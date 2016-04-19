package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int appleP_x;                                            //+++++++++++
    private int appleP_y;                                            //+++++++++++

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image appleP;                                                       //++++++++++++
    private Image apple;
    private Image head;
    private Image bg;
    private Image gameOver;

    private int length = 3;

    public Board() {

        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.loadImages();
        this.initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("resources/dot.png");
        ball = iid.getImage();

        ImageIcon iiap = new ImageIcon("resources/appleP.png");
        appleP = iiap.getImage();

        ImageIcon iia = new ImageIcon("resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("resources/head.png");
        head = iih.getImage();

        ImageIcon iib = new ImageIcon("resources/background.jpg");
        bg = iib.getImage();

        ImageIcon iiGameOver = new ImageIcon("resources/Game-over-poisoned.png");
        gameOver = iiGameOver.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        score(g);
        doDrawing(g);
    }

    private void score(Graphics g) {
        String msg = "Score: " + String.valueOf(this.dots);
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)), 20);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            if (dots >= 5) {
                g.drawImage(appleP, appleP_x, appleP_y, this);                      //++++++++++++
            }

            g.drawImage(apple, apple_x, apple_y, this);
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        g.drawImage(this.gameOver, ((B_WIDTH / 2) - 178 / 2), ((B_HEIGHT / 2 )- 178 / 2), this);

        /* draw text
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.red);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        */
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
            locateAppleP();                                                  //++++++++++++++
        }
    }

    private void checkAppleP() {                                             //++++++++++++++

        if ((x[0] == appleP_x) && (y[0] == appleP_y)) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    private void locateAppleP() {                                                //++++++++++++++

        int r = (int) (Math.random() * RAND_POS);
        appleP_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        appleP_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkAppleP();                                                   //++++++++++++++
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}