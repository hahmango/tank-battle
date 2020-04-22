#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <fcntl.h>
#include <errno.h>

#define NEW_CONNECT 0x00010000 // 字节序小端>大端 反码 0x00000100
#define SET_TANK_ID 0x00020000 // 0x00000200
#define RUNNING 0x00040000
#define OVER 0x00080000
#define QUIT 0x00100000

const int GAME_RUNNING = 1;
const int GAME_OVER = 0;
int status;
typedef struct link_list
{
	int acp_fd;
	struct link_list *next;
} Node;
Node *head = NULL;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int initserver(char *port)
{
	int tcp_socket;
	struct sockaddr_in sockaddr;

	head = (Node *)malloc(sizeof(Node));
	head->next = NULL;
	head->acp_fd = -1;

	if ((tcp_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		perror("socket error");
		printf("errno = %d", errno);
		exit(1);
	}

	sockaddr.sin_family = AF_INET;
	sockaddr.sin_port = htons(atoi(port));
	sockaddr.sin_addr.s_addr = INADDR_ANY;

	if (bind(tcp_socket, (const struct sockaddr *)&sockaddr, sizeof(struct sockaddr_in)) < 0)
	{
		perror("bind error");
		exit(1);
	}

	if (listen(tcp_socket, 1) < 0)
	{
		perror("listen error");
		exit(1);
	}

	return tcp_socket;
}

int Accept(int tcp_socket)
{
	int acp_fd;
	if ((acp_fd = accept(tcp_socket, NULL, NULL)) < 0)
	{
		perror("accept error");
		// exit(1);
	}
	return acp_fd;
}

void linklist_add(int fd)
{
	Node *newNode = NULL;
	newNode = (Node *)malloc(sizeof(Node));
	newNode->acp_fd = fd;
	newNode->next = NULL;
	if (head->next != NULL)
		newNode->next = head->next;
	head->next = newNode;
}

void linklist_dele(int fd)
{
	Node *pre = head, *cur = head->next;
	while (cur->acp_fd != fd)
	{
		pre = cur;
		cur = cur->next;
	}
	if (cur->next == NULL)
		pre->next = NULL;
	else
		pre->next = cur->next;
	free(cur);
}

void doWork(int acp_fd)
{
	int readBuf[2], readNum, intData[2];
	Node *cur;
	while ((readNum = recv(acp_fd, readBuf, sizeof(int), MSG_WAITALL)) > 0)
	{
		if (readBuf[0] & ntohl(RUNNING))
		{
			status = GAME_RUNNING;
			continue;
		}
		else if (readBuf[0] & ntohl(OVER))
		{
			status = GAME_OVER;
			continue;
		}
		if (readBuf[0] & ntohl(NEW_CONNECT))
		{
			intData[0] = htonl(acp_fd - 4 | SET_TANK_ID);
			printf("New connection, create new tank\n");
			write(acp_fd, intData, sizeof(int));
		}
		// send data to all acception's fd
		cur = head;
		do
		{
			cur = cur->next;
			write(cur->acp_fd, (const int *)&readBuf, readNum);
		} while (cur->next != NULL);
	}
}

void sendQuit(int acp_fd)
{
	int intData[2];
	Node *cur;

	intData[0] = htonl(QUIT | acp_fd - 4);
	if (head->next != NULL)
	{
		cur = head;
		do
		{
			cur = cur->next;
			write(cur->acp_fd, intData, sizeof(int));
		} while (cur->next != NULL);
	}
	else
		status = GAME_OVER;
}

// thread accept
void *handle(void *fd)
{
	int tcp_socket;
	int acp_fd;

	tcp_socket = *((int *)fd);
	while (1)
	{
		pthread_mutex_lock(&mutex);
		acp_fd = Accept(tcp_socket);
		pthread_mutex_unlock(&mutex);
		if(acp_fd < 0)
			continue;
		linklist_add(acp_fd);

		if (status == GAME_RUNNING)
		{
			// 游戏运行中，禁止连接
			linklist_dele(acp_fd);
			close(acp_fd);
			// sendQuit(acp_fd);
			continue;
		}
		printf("I am thread %lu, one client has connected, descriptor is %d.\n", (unsigned long)pthread_self(), acp_fd);
		doWork(acp_fd);
		printf("I am thread %lu, client %d 's connection is ending.\n", (unsigned long)pthread_self(), acp_fd);
		linklist_dele(acp_fd);
		close(acp_fd);
		sendQuit(acp_fd);
	}
}

void create_threadpool(int tcp_socket)
{
	int threadNum = 8, i;
	pthread_t thread[threadNum];
	pthread_attr_t attr;

	// set thread's attr to avid zombie thread
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);

	for (i = 0; i < threadNum; i++)
	{
		pthread_create(&(thread[i]), &attr, handle, &tcp_socket);
	}
}

int main(int argc, char *argv[])
{
	int tcp_socket;

	if (argc != 2)
	{
		perror("Usage : CMD portnum");
		exit(1);
	}

	tcp_socket = initserver(argv[1]);

	status = GAME_OVER;
	create_threadpool(tcp_socket);
	pause();

	return 0;
}