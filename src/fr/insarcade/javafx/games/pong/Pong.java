/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.pong;

import fr.insarcade.controller.ControllerData;
import fr.insarcade.javafx.core.Arcade;
import fr.insarcade.javafx.core.ArcadeGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class Pong extends ArcadeGame {
    
    private static final int AREA_X1 = 100;
    private static final int AREA_Y1 = 150;
    private static final int AREA_X2 = 900;
    private static final int AREA_Y2 = 700;
    private static final double SIZE_STICK = (AREA_X2-AREA_X1) /80;
    private static final double VSTICKS = 1000;
    private static final double XMIN = AREA_X1 + SIZE_STICK;
    private static final double YMIN = AREA_Y1 + SIZE_STICK;
    private static final double XMAX = AREA_X2 - SIZE_STICK;
    private static final double YMAX = AREA_Y2 - SIZE_STICK;
    
    private byte level;
    private Stick stick1, stick2, stick3, stick4;
    private Ball ball, previousBall,tmp;
    private final double[] levelUp;
    private final Image[] images;
    private double out=0;
    
    public Pong(){
        super("Pong","David Albert");
        levelUp = new double[2];
        images = new Image[3];
        images[0] = new Image("res/Pong/ball.png");
        level = 1;
        ball = new Ball();
        initSticks();
    }
    
    @Override
    public void resetGame() {
        level = (byte)(getScore_PLAYER1()+getScore_PLAYER2()+1);;
        levelUp[0]=500;
        levelUp[1]=0;
        ball = new Ball();
        previousBall = new Ball();
        tmp = new Ball();
        initSticks();
        setState(1);
    }
    
    @Override
    public void updateGame(double interpolation, ControllerData controller) {
        switch (getState()) {
            case 1 :
                if (levelUp[0] >= AREA_X1+(AREA_X2-AREA_X1)/4)
                {levelUp[0]-=500*interpolation;}
                else 
                {levelUp[1]+=interpolation;}
                
                if (levelUp[1] > 3.5)
                {
                    if (level%2==0)
                        ball.setPosition(ball.getX()+ball.getSpeed()*interpolation, ball.getY()+ball.getSpeed()*interpolation);
                    else
                        ball.setPosition(ball.getX()- (ball.getSpeed()*interpolation), ball.getY()-(ball.getSpeed()*interpolation));
                    setState(2);
                }
                break;
            
            case 2: 
                if (controller.isJoystick1_UP() && stick1.getY() > YMIN ) {
                        stick1.setY(-(interpolation * VSTICKS));
                }
                if (controller.isJoystick1_DOWN() && stick1.getY()+ stick1.getHeight()+10 < YMAX) {
                    stick1.setY(interpolation * VSTICKS);
                }
                if (controller.isJoystick2_UP() && stick2.getY() > YMIN) {
                    stick2.setY(-(interpolation * VSTICKS));
                }
                if (controller.isJoystick2_DOWN() && stick2.getY()+ stick2.getHeight()+10 < YMAX) {
                    stick2.setY(interpolation * VSTICKS);
                }
                if (controller.isJoystick1_LEFT() && stick3.getX() > XMIN ) {
                     stick3.setX(-(interpolation * VSTICKS));
                }
                if (controller.isJoystick1_RIGHT() && stick3.getX()+ stick3.getWidth()+10 < XMAX) {
                    stick3.setX(interpolation * VSTICKS);
                }
                if (controller.isJoystick2_LEFT() && stick4.getX() > XMIN) {
                    stick4.setX(-(interpolation * VSTICKS));
                }
                if (controller.isJoystick2_RIGHT() && stick4.getX()+ stick4.getWidth()+10 < XMAX) {
                    stick4.setX(interpolation * VSTICKS);
                }
                tmp.setPosition(tmp.getX()+(ball.getX()-previousBall.getX()), tmp.getY()+(ball.getY()-previousBall.getY()));
                calculerNewPosition(interpolation);
                previousBall.setPosition(previousBall.getX()+(tmp.getX()-previousBall.getX()),previousBall.getY()+(tmp.getY()- previousBall.getY()));
                ball.increseSpeed();
                if (end()) {
                    if ((ball.getX()+ball.getRadius()>XMAX) || (ball.getY()+ball.getRadius()> YMAX )) 
                         setScore_PLAYER1(getScore_PLAYER1() + 1);
                    else setScore_PLAYER2(getScore_PLAYER2() + 1);
                    setState(3);
                }
                
                break;
                
            case 3 :
                if (out < 2)
                {
                    out+=interpolation;
                }
                else
                {
                    out =0;
                    if (level == 5)
                        setState(100);
                    else
                    {
                    level++;
                    resetGame();
                    setState(1);
                    }
                }
                break;
            case 100 : 
                if (getScore_PLAYER1() > getScore_PLAYER2())
                    gameIsFinish(winner.Player1, true);
                else 
                     gameIsFinish(winner.Player2, true);
            break;
        }
    }
    
    private void initSticks(){
        
        switch (level) {
            case 1  :
                stick1 = new Stick(AREA_X1,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick2 = new Stick(XMAX,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick3 = new Stick(AREA_X1,AREA_Y1,SIZE_STICK,AREA_X2-AREA_X1);
                stick4 = new Stick(AREA_X1,YMAX,SIZE_STICK,AREA_X2-AREA_X1);
                break;
            case 2  :
                stick1 = new Stick(AREA_X1,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick2 = new Stick(XMAX,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick3 = new Stick(AREA_X1,AREA_Y1,SIZE_STICK,AREA_X2-AREA_X1);
                stick4 = new Stick(AREA_X1,YMAX,SIZE_STICK,AREA_X2-AREA_X1);
                break;
            case 3 :
                stick1 = new Stick(AREA_X1,0.5*AREA_Y2,AREA_Y2/4,SIZE_STICK);
                stick2 = new Stick(XMAX,0.5*AREA_Y2,AREA_Y2/4,SIZE_STICK);
                stick3 = new Stick(AREA_X1,AREA_Y1,SIZE_STICK,AREA_X2-AREA_X1);
                stick4 = new Stick(AREA_X1,YMAX,SIZE_STICK,AREA_X2-AREA_X1);
                break;
            case 4 :
                stick1 = new Stick(AREA_X1,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick2 = new Stick(XMAX,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick3 = new Stick(0.3*AREA_X2,AREA_Y1,SIZE_STICK,AREA_X2/2);
                stick4 = new Stick(0.3*AREA_X2,YMAX,SIZE_STICK,AREA_X2/2);
                break;
            case 5 :
                stick1 = new Stick(AREA_X1,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick2 = new Stick(XMAX,0.4*AREA_Y2,AREA_Y2/3,SIZE_STICK);
                stick3 = new Stick(0.35*AREA_X2,AREA_Y1,SIZE_STICK,AREA_X2/3);
                stick4 = new Stick(0.35*AREA_X2,YMAX,SIZE_STICK,AREA_X2/3);
                break;
            default :
                stick1 = new Stick(0,Arcade.getWindowHeight()/3,Arcade.getWindowHeight()/3,SIZE_STICK);
                stick2 = new Stick(XMAX,Arcade.getWindowHeight()/3,Arcade.getWindowHeight()/3,SIZE_STICK);
                stick3 = new Stick(Arcade.getWindowWidth()/3,0,SIZE_STICK,Arcade.getWindowWidth()/3);
                stick4 = new Stick(Arcade.getWindowWidth()/3,YMAX,SIZE_STICK,Arcade.getWindowWidth()/3);
                break;
    }
       
    }
    
    private boolean isCollision(){
        return (stick1.collisionBall(ball) || stick2.collisionBall(ball) || stick3.collisionBall(ball) || stick4.collisionBall(ball));
    }
     
    public void calculerNewPosition(double interpolation){
        if (stick1.collisionBall(ball))
            {newXY(stick1,interpolation);}
        else if (stick2.collisionBall(ball))
            {newXY(stick2,interpolation);}
         else if (stick3.collisionBall(ball))
            {newXY(stick3,interpolation);}
         else if (stick4.collisionBall(ball))
            {newXY(stick4,interpolation);}
         else 
             ball.setPosition( ball.getX()+ball.getSpeed()*interpolation*coefOrientation(previousBall.getX(),ball.getX(),previousBall.getY(),ball.getY())  ,  ball.getY()+ball.getSpeed()*interpolation*coefOrientation(previousBall.getY(),ball.getY(),previousBall.getX(),ball.getX()));
        
    }
    
    private double coefOrientation(double valeur0, double valeur1,double comparateur0,double comparateur1){
        return (valeur1 - valeur0)/(Math.abs(valeur1-valeur0)+ Math.abs(comparateur1-comparateur0));
    }
    
    private void newXY(Stick s,double interpolation){
        double teta, teta1, sommeT;
        double z;
        z = Math.sqrt(Math.pow(ball.getX()-previousBall.getX(),2)+Math.pow(ball.getY()-previousBall.getY(),2));
         //angle d'incidence
         //angle engendré par le coté du stick touché
        
        
        if (s.equals(stick4))
        {
            teta = Math.asin((ball.getX()-previousBall.getX())/z);
            teta1 = (((ball.getX()-s.getX())/s.getWidth())*Math.PI*0.5)-(0.25*Math.PI);
            sommeT = 0.7*(teta +teta1);
            if (sommeT>1.134)
               sommeT = 1.134;
            else if (sommeT<-1.134)
               sommeT = -1.134;
            if (level <=3) 
                ball.setPosition(ball.getX()+z*Math.sin(teta), ball.getY()-z*Math.cos(teta));
            else 
                ball.setPosition(ball.getX()+z*Math.sin(sommeT), ball.getY()-z*Math.cos(sommeT));
            System.out.println("z = "+z+ "    teta = "+(teta*180/Math.PI)+ "    teta1 = "+ (teta1*180/Math.PI));
        }
        if (s.equals(stick3))
        {
            teta = Math.asin((ball.getX()-previousBall.getX())/z);
            teta1 = (((ball.getX()-s.getX())/s.getWidth())*Math.PI*0.5)-(0.25*Math.PI);
            sommeT = 0.7*(teta +teta1);
            if (sommeT>1.134)
               sommeT = 1.134;
            else if (sommeT<-1.134)
               sommeT = -1.134;
            if (level <=3) 
                ball.setPosition(ball.getX()+z*Math.sin(teta), ball.getY()+z*Math.cos(teta));
            else 
                ball.setPosition(ball.getX()+z*Math.sin(sommeT), ball.getY()+z*Math.cos(sommeT));
            System.out.println("z = "+z+ "    teta = "+(teta*180/Math.PI)+ "    teta1 = "+ (teta1*180/Math.PI));
        }
        if (s.equals(stick2))
        {
            teta = Math.asin((ball.getY()-previousBall.getY())/z);
            teta1 = (((ball.getY()-s.getY())/s.getHeight())*Math.PI*0.8)-(0.4*Math.PI);
            sommeT = 0.7*(teta +teta1);
            if (sommeT>1.134)
               sommeT = 1.134;
            else if (sommeT<-1.134)
               sommeT = -1.134;
            ball.setPosition(ball.getX()-z*Math.cos(sommeT), ball.getY()+z*Math.sin(sommeT));
        }
        if (s.equals(stick1))
        {
            teta = Math.asin((ball.getY()-previousBall.getY())/z);
            teta1 = (((ball.getY()-s.getY())/s.getHeight())*Math.PI*0.8)-(0.4*Math.PI);
            sommeT = 0.7*(teta +teta1);
            if (sommeT>1.134)
               sommeT = 1.134;
            else if (sommeT<-1.134)
               sommeT = -1.134;
            ball.setPosition(ball.getX()+z*Math.cos(sommeT), ball.getY()+z*Math.sin(sommeT));
        }
            
    }
    private boolean end(){
        return (!(isCollision()) && ((ball.getX()<=AREA_X1-20) || (ball.getX()+ball.getRadius()>AREA_X2+20) || (ball.getY()<=AREA_Y1-20) || (ball.getY()+ball.getRadius()>=AREA_Y2+20) ) );
    }
    
    
    @Override
    public void renderGame(GraphicsContext drawContext) {
        switch (getState()) {
            case 1: 
               drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
               drawContext.setFill(Color.color(0.4, 0.4, 0.4, 0.8));
               drawContext.setStroke(Color.color(1,1,1,0.9));
               drawContext.fillRoundRect(XMIN, 50, XMAX -XMIN,50, 20, 20);
               drawContext.strokeRoundRect(XMIN, 50, XMAX -XMIN,50, 20, 20);
               
               drawContext.setFill(Color.color(0.1, 0.1, 0.3, 0.8));
               drawContext.fillRect(AREA_X1-20,AREA_Y1-20,AREA_X2-AREA_X1+40,AREA_Y2-AREA_Y1+40);
               drawContext.strokeRect(AREA_X1-20,AREA_Y1-20,AREA_X2-AREA_X1+40,AREA_Y2-AREA_Y1+40);
               
               
               drawContext.setFont(Arcade.FONT_40());
               
               drawContext.setFill(Color.color(0,0,1,0.8));
               drawContext.fillText("" + getScore_PLAYER1(), XMIN+50, 90);
               drawContext.setFill(Color.color(1,0,0,0.8));
               drawContext.fillText("" + getScore_PLAYER2(), XMAX-60, 90);
               
               drawContext.setFill(Color.color(1,1,1,0.8));
               drawContext.fillRect(stick1.getX(),stick1.getY(),stick1.getWidth(),stick1.getHeight()); 
               drawContext.fillRect(stick2.getX(),stick2.getY(),stick2.getWidth(),stick2.getHeight());
               drawContext.fillRect(stick3.getX(),stick3.getY(),stick3.getWidth(),stick3.getHeight());
               drawContext.fillRect(stick4.getX(),stick4.getY(),stick4.getWidth(),stick4.getHeight());
               
               drawContext.fillRect(Arcade.getWindowWidth()/2,AREA_Y1,2,AREA_Y2-AREA_Y1);
               
               drawContext.setFill(Color.gray(0.9));
               drawContext.setFont(Arcade.FONT_70());
               if (levelUp[0]>AREA_X1+(AREA_X2-AREA_X1)/4 || levelUp[1]<1){
                   drawContext.fillText(" ROUND "+ this.level , levelUp[0],AREA_Y1+(AREA_Y2-AREA_Y1)/2);
               }
               else if(levelUp[1]<1.5)
                   drawContext.fillText("3", (AREA_X2-AREA_X1)/2,AREA_Y1+(AREA_Y2-AREA_Y1)/2);
               else if (levelUp[1]<2)
                   drawContext.fillText("2", (AREA_X2-AREA_X1)/2,AREA_Y1+(AREA_Y2-AREA_Y1)/2);
               else if (levelUp[1]<2.5)
                   drawContext.fillText("1", (AREA_X2-AREA_X1)/2,AREA_Y1+(AREA_Y2-AREA_Y1)/2);
               else if (levelUp[1]<3.5)
                   drawContext.fillText("GO !!! ", (AREA_X2-AREA_X1)/3,AREA_Y1+(AREA_Y2-AREA_Y1)/2);
               else
                   drawContext.fillText("", levelUp[0],levelUp[1]);
               break;
            default :
               drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
               
               //affichage table 
               drawContext.setStroke(Color.WHITE);
               drawContext.setFill(Color.color(0.4, 0.4, 0.4, 1));
               drawContext.fillRoundRect(XMIN, 50, XMAX -XMIN,50, 20, 20);
               drawContext.strokeRoundRect(XMIN, 50, XMAX -XMIN,50, 20, 20);
               
               drawContext.setFill(Color.color(0.1, 0.1, 0.3, 1));
               drawContext.fillRect(AREA_X1-20,AREA_Y1-20,AREA_X2-AREA_X1+40,AREA_Y2-AREA_Y1+40);
               drawContext.strokeRect(AREA_X1-20,AREA_Y1-20,AREA_X2-AREA_X1+40,AREA_Y2-AREA_Y1+40);
               
               //affichage score
               drawContext.setFont(Arcade.FONT_40());
               drawContext.setFill(Color.BLUE);
               drawContext.fillText("" + getScore_PLAYER1(), XMIN+50, 90);
               drawContext.setFill(Color.RED);
               drawContext.fillText("" + getScore_PLAYER2(), XMAX-60, 90);
               
               //affichage éléments
               drawContext.setFill(Color.WHITE);
               drawContext.fillRect(stick1.getX(),stick1.getY(),stick1.getWidth(),stick1.getHeight()); 
               drawContext.fillRect(stick2.getX(),stick2.getY(),stick2.getWidth(),stick2.getHeight());
               drawContext.fillRect(stick3.getX(),stick3.getY(),stick3.getWidth(),stick3.getHeight());
               drawContext.fillRect(stick4.getX(),stick4.getY(),stick4.getWidth(),stick4.getHeight());
               drawContext.fillRect(Arcade.getWindowWidth()/2,AREA_Y1,2,AREA_Y2-AREA_Y1);
               drawContext.drawImage(images[0],ball.getX(), ball.getY(), ball.getRadius(),ball.getRadius());
               if (getState()==3)
               {    
                   drawContext.setFont(Arcade.FONT_70());
                   drawContext.setFill(Color.color(0.8, 0, 0));
                   drawContext.fillText("OUT !!!" , 430, 420);
                   drawContext.strokeText("OUT !!!" , 430, 420);
               }
               break;
        }
       }
} 
   

