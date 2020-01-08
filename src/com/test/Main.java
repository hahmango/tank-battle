package com.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import org.loon.framework.game.simple.GameScene;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.Deploy;
import org.loon.framework.game.simple.core.graphics.Screen;
import org.loon.framework.game.simple.core.timer.LTimerContext;

/**
 * Copyright 2008 - 2009
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author chenpeng
 * @version 0.1
 * @project loonframework
 * @email：ceponline@yahoo.com.cn
 */
public class Main extends Screen {

    private double dx;

    private double dy;

    private double vdx;

    private double vdy;

    private double jx;

    private double jy;

    private double vjx;

    private double vjy;

    private double ox;

    private double oy;

    private double df;

    private double d5;

    private int scj;

    private int sco;

    private double d = 0.0D;

    private double d1 = 0.0D;

    private float[] af = new float[3000];

    private boolean flag;

    private Synthesizer synthesizer = null;

    private MidiChannel midichannel = null;

    public Main() {
        dx = 350D;
        dy = 207D;
        ox = 350D;
        oy = 730D;
        jx = 350D;
        jy = 60D;
        df = (int) ((double) (getWidth() / 2) / Math.tan(0.78539816339744828D));
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midichannel = synthesizer.getChannels()[0];
            flag = true;
        } catch (Exception ex) {
        }
        d5 = (double) (getHeight() / 2) - (double) (getHeight() / 2 - 770) * df;
    }

    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GRAY);
        for (int i = 0; i < 750; i++) {
            double d2 = (double) (20 + i) + df;
            g
                    .drawRect(
                            (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 50) * df)
                                    / d2), (int) (d5 / d2),
                            (int) ((600D * df) / d2), 1);
        }

        g.setColor(Color.DARK_GRAY);
        g
                .fillRect(
                        (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 50) * df)
                                / (20D + df)), (int) (d5 / (20D + df)),
                        (int) ((600D * df) / (20D + df)) + 1, 5);
        g.setColor(new Color(0.0F, 0.8F, 0.0F));
        double d3 = 20D + df;
        g
                .fill3DRect(
                        (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 250) * df)
                                / d3), (int) (d5 / d3) - 5,
                        (int) ((200D * df) / d3), (int) ((10D * df) / d3), true);
        d3 = 770D + df;
        g
                .fill3DRect(
                        (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 250) * df)
                                / d3), (int) (d5 / d3),
                        (int) ((200D * df) / d3), (int) ((10D * df) / d3), true);
        d3 = 395D + df;
        g
                .fillRect(
                        (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 50) * df)
                                / d3), (int) (d5 / d3),
                        (int) ((600D * df) / d3), 2);
        d3 = 404D + df;
        g
                .fillArc(
                        (int) ((double) (getWidth() / 2) - ((double) (getWidth() / 2 - 330) * df)
                                / d3), (int) (d5 / d3),
                        (int) ((40D * df) / d3),
                        (int) ((40D * df) / (2D * d3)), 0, 360);
        for (int j = 0; j < af.length; j += 3)
            if ((double) af[j + 2] > 0.5D) {
                g.setColor(new Color(af[j + 2], af[j + 2], 0.0F));
                d3 = (double) af[j + 1] + df;
                g
                        .drawRect(
                                (int) ((double) (getWidth() / 2) - ((double) ((float) (getWidth() / 2) - af[j]) * df)
                                        / d3),
                                (int) ((d5 + (double) LSystem.random.nextInt(8)) / d3),
                                0, 0);
                af[j] += (double) (LSystem.random.nextFloat() * 5F) - 2.5D;
                af[j + 1] += (double) (LSystem.random.nextFloat() * 5F) - 2.5D;
                af[j + 2] -= 0.01D;
            }

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLUE);
        for (int k = 0; k < scj; k++) {
            g.fillOval(20 + 25 * k, getHeight() - 40, 20, 20);
        }
        for (int i1 = 0; i1 < sco; i1++) {
            g.fillOval(20 + 25 * i1, 20, 20, 20);
        }
        d3 = dy + 7D + df;
        g
                .fillArc(
                        (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (dx - 15D)) * df)
                                / d3), (int) (d5 / d3),
                        (int) ((30D * df) / d3),
                        (int) ((30D * df) / (2D * d3)), 0, 360);
        g.setColor(Color.CYAN);
        g
                .fillArc(
                        (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (dx - 15D)) * df)
                                / d3), (int) (d5 / d3) - 2,
                        (int) ((30D * df) / d3),
                        (int) ((30D * df) / (2D * d3)), 0, 360);
        g.setColor(new Color(127, 124, 123));
        if (scj == 7) {
            int j1 = 0;
            for (int j2 = 0; j2 < af.length && j1 < 60; j2 += 3)
                if ((double) af[j2 + 2] <= 0.5D) {
                    af[j2] = 50 + LSystem.random.nextInt(600);
                    af[j2 + 1] = 20 + LSystem.random.nextInt(375);
                    af[j2 + 2] = 1.0F;
                    j1++;
                }
            g.setFont(g.getFont().deriveFont(1, 25F));
            g.drawString("YOU ARE THE WINNER!", getWidth() / 2 - 150,
                    getHeight() / 2);
            if (flag) {
                midichannel.noteOff(LSystem.random.nextInt(128), LSystem.random
                        .nextInt(128));
                midichannel.noteOn(LSystem.random.nextInt(128), LSystem.random
                        .nextInt(128));
            }
        } else if (sco == 7) {
            int k1 = 0;
            for (int k2 = 0; k2 < af.length && k1 < 60; k2 += 3)
                if ((double) af[k2 + 2] <= 0.5D) {
                    af[k2] = 50 + LSystem.random.nextInt(600);
                    af[k2 + 1] = 395 + LSystem.random.nextInt(375);
                    af[k2 + 2] = 1.0F;
                    k1++;
                }

            g.drawString("Press space to try again or escape to exit", 10,
                    getHeight() - 45);
            g.setFont(g.getFont().deriveFont(1, 25F));
            g.drawString("YOU HAVE LOST THE MATCH", getWidth() / 2 - 190,
                    getHeight() / 2);
            if (flag) {
                midichannel.noteOff(LSystem.random.nextInt(128), LSystem.random
                        .nextInt(128));
                midichannel.noteOn(LSystem.random.nextInt(128), LSystem.random
                        .nextInt(128));
            }
        } else {
            double d4 = jy + 10D + df;
            g
                    .fillArc(
                            (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (jx - 20D)) * df)
                                    / d4), (int) (d5 / d4),
                            (int) ((40D * df) / d4),
                            (int) ((40D * df) / (2D * d4)), 0, 360);
            g.setColor(Color.RED);
            g
                    .fillArc(
                            (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (jx - 20D)) * df)
                                    / d4), (int) (d5 / d4) - 2,
                            (int) ((40D * df) / d4),
                            (int) ((40D * df) / (2D * d4)), 0, 360);
            g.setColor(new Color(127, 24, 39));
            d4 = oy + 10D + df;
            g
                    .fillArc(
                            (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (ox - 20D)) * df)
                                    / d4), (int) (d5 / d4),
                            (int) ((40D * df) / d4),
                            (int) ((40D * df) / (2D * d4)), 0, 360);
            g.setColor(Color.RED);
            g
                    .fillArc(
                            (int) ((double) (getWidth() / 2) - (((double) (getWidth() / 2) - (ox - 20D)) * df)
                                    / d4), (int) (d5 / d4) - 2,
                            (int) ((40D * df) / d4),
                            (int) ((40D * df) / (2D * d4)), 0, 360);
            if (dx < 50D || dx > 650D) {
                vdx = -vdx;
                dx = (dx >= 50D ? 599 : '\001') + 50;
                if (flag) {
                    midichannel.allNotesOff();
                    midichannel.noteOn(120, 127);
                }
                int l1 = 0;
                for (int l2 = 0; l2 < af.length && l1 < 60; l2 += 3)
                    if ((double) af[l2 + 2] <= 0.5D) {
                        af[l2] = (float) dx;
                        af[l2 + 1] = (float) dy;
                        af[l2 + 2] = 1.0F;
                        l1++;
                    }

            }
            if (dx > 250D && dx < 450D && (dy <= 20D || dy >= 770D)) {
                if (dy < 395D) {
                    sco++;
                    dy = 582D;
                    oy = 730D;
                } else {
                    scj++;
                    dy = 207D;
                }
                dx = 350D;
                vdx = 0.0D;
                vdy = 0.0D;
                if (flag) {
                    midichannel.allNotesOff();
                    midichannel.noteOn(110, 127);
                    midichannel.noteOn(106, 127);
                }
            } else if (dy < 20D || dy > 770D) {
                vdy = -vdy;
                dy = (dy >= 20D ? 750 : '\0') + 20;
                if (flag) {
                    midichannel.allNotesOff();
                    midichannel.noteOn(120, 127);
                }
                int i2 = 0;
                for (int i3 = 0; i3 < af.length && i2 < 60; i3 += 3) {
                    if ((double) af[i3 + 2] <= 0.5D) {
                        af[i3] = (float) dx;
                        af[i3 + 1] = (float) dy;
                        af[i3 + 2] = 1.0F;
                        i2++;
                    }
                }

            }
            d4 = Math.sqrt((dx - jx) * (dx - jx) + (dy - jy) * (dy - jy));
            if (d4 < 35D) {
                double d6 = Math.sqrt(vjx * vjx + vjy * vjy);
                double d8 = Math.sqrt(vdx * vdx + vdy * vdy);
                double d10 = Math.atan2(dy - jy, dx - jx);
                double d12 = Math
                        .abs(d6
                                * ((vjx * (dx - jx) + vjy * (dy - jy)) / (d6
                                * d4 + 0.001D))
                                + d8
                                * ((vdx * (dx - jx) + vdy * (dy - jy)) / (d8
                                * d4 + 0.001D)));
                vdx = Math.abs(d12 * Math.cos(d10)) <= 40D ? d12
                        * Math.cos(d10) : 40D * Math
                        .signum(d12 * Math.cos(d10));
                vdy = Math.abs(d12 * Math.sin(d10)) <= 40D ? d12
                        * Math.sin(d10) : 40D * Math
                        .signum(d12 * Math.sin(d10));
                dx += ((40D - d4) + 2D) * Math.cos(d10);
                dy += ((40D - d4) + 2D) * Math.sin(d10);
                if (flag) {
                    midichannel.allNotesOff();
                    midichannel.noteOn(127, 127);
                }
                int j3 = 0;
                for (int l3 = 0; l3 < af.length && j3 < 100; l3 += 3) {
                    if ((double) af[l3 + 2] <= 0.5D) {
                        af[l3] = (float) dx;
                        af[l3 + 1] = (float) dy;
                        af[l3 + 2] = 1.0F;
                        j3++;
                    }
                }
            }
            jx += vjx;
            jy += vjy;
            vjx = 0.0D;
            vjy = 0.0D;
            d4 = Math.sqrt((dx - ox) * (dx - ox) + (dy - oy) * (dy - oy));
            if (d4 < 35D) {
                double d7 = Math.sqrt(d * d + d1 * d1);
                double d9 = Math.sqrt(vdx * vdx + vdy * vdy);
                double d11 = Math.atan2(dy - oy, dx - ox);
                double d13 = Math
                        .abs(d7
                                * ((d * (dx - ox) + d1 * (dy - oy)) / (d7 * d4 + 0.001D))
                                + d9
                                * ((vdx * (dx - ox) + vdy * (dy - oy)) / (d9
                                * d4 + 0.001D)));
                vdx = Math.abs(d13 * Math.cos(d11)) <= 40D ? d13
                        * Math.cos(d11) : 40D * Math
                        .signum(d13 * Math.cos(d11));
                vdy = Math.abs(d13 * Math.sin(d11)) <= 40D ? d13
                        * Math.sin(d11) : 40D * Math
                        .signum(d13 * Math.sin(d11));
                dx += ((40D - d4) + 2D) * Math.cos(d11);
                dy += ((40D - d4) + 2D) * Math.sin(d11);
                if (flag) {
                    midichannel.allNotesOff();
                    midichannel.noteOn(127, 127);
                }
                int k3 = 0;
                for (int i4 = 0; i4 < af.length && k3 < 100; i4 += 3)
                    if ((double) af[i4 + 2] <= 0.5D) {
                        af[i4] = (float) dx;
                        af[i4 + 1] = (float) dy;
                        af[i4 + 2] = 1.0F;
                        k3++;
                    }

            }
            dx += vdx;
            dy += vdy;
            if (Math.abs(dx - ox) < 15D) {
                d = 0.0D;
            } else {
                d += dx >= ox ? 0.5D : -0.5D;
            }
            if (dy > 395D) {
                if (Math.abs(dx - ox) < 40D && dy < oy)
                    d1 -= 0.5D;
                else if (dy > oy)
                    d1 += 0.5D;
                else
                    d1 = 0.0D;
            } else {
                d1 += oy >= 590D ? -0.5D : 0.5D;
            }
            ox += d;
            oy += d1;
            if (ox < 50D) {
                ox = 50D;
            } else if (ox > 650D) {
                ox = 650D;
            }
            if (oy < 395D) {
                oy = 395D;
            } else if (oy > 770D) {
                oy = 772D;
            }

        }
    }

    public void alter(LTimerContext t) {
        if (this.getMouseX() == 0) {
            return;
        }
        double d = ((double) (getHeight() / 2) - (double) (getHeight() / 2 - 770)
                * df)
                / (double) getMouseY() - df;
        double d1 = (double) (getWidth() / 2)
                + ((d + df) * (double) (getMouseX() - getWidth() / 2)) / df;
        if (d1 < 50D) {
            d1 = 50D;
        } else if (d1 > 650D) {
            d1 = 650D;
        }
        if (d > 395D) {
            d = 395D;
        } else if (d < 20D) {
            d = 20D;
        }
        vjx = d1 - jx;
        vjy = d - jy;
    }

    public void leftClick(MouseEvent e) {

    }

    public void middleClick(MouseEvent e) {

    }

    public void rightClick(MouseEvent e) {

    }


    public void onKey(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            dx = 350D;
            dy = 394D;
            ox = 350D;
            oy = 730D;
            scj = sco = 0;
        }
    }

    public void onKeyUp(KeyEvent e) {

    }

    public static void main(String[] args) {
        GameScene frame = new GameScene("Java版碰撞球(Applet小游戏移植)", 700, 600);
        Deploy deploy = frame.getDeploy();
        deploy.setScreen(new Main());
        deploy.setFPS(40);
        deploy.setShowFPS(true);
        deploy.setLogo(false);
        deploy.mainLoop();
        frame.showFrame();
    }

}
