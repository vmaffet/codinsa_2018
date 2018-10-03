package fr.insalyon.hexbomber;

import java.util.LinkedList;
import java.util.Scanner;

public class Inputs {
    Scanner sc;
    private int idJoueur;
    private int round;
    private int radius;
    //tableaux avec [0] pour joueur 0,[1] pour joueur1
    
    public Inputs(){
        sc=new Scanner(System.in);
    }

    public void readInputs(Board board) {
        idJoueur=sc.nextInt();
        round=sc.nextInt();
        radius=sc.nextInt();
        
        if(board.getRadius() == 0){
            board.setRadius(radius);
        }
        
        board.reset();

        board.setRound(round);

        sc.nextLine(); //flush
        String infoPlayer[]=sc.nextLine().split(";");
        for(int i=0;i<infoPlayer.length;i++) {
            board.setPlayer(i,extractDataPlayer(infoPlayer[i]));
        }
        board.setMyselfFirst(idJoueur);
        String infoBomb[]=sc.nextLine().split(";");
        for(int i=0;i<infoBomb.length;i++) {
            if (!infoBomb[i].isEmpty()) {
                board.addBomb(extractDataBomb(infoBomb[i]));
            }
        }
        
        String infoBonus[]=sc.nextLine().split(";");
        for(int i=0;i<infoBonus.length;i++) {
            if (!infoBonus[i].isEmpty()) {
                board.addBonus(extractDataBonus(infoBonus[i]));
            }
        }
        
        String infoWoodBoxes[]=sc.nextLine().split(";");
        for(int i=0;i<infoWoodBoxes.length;i++) {
            if (!infoWoodBoxes[i].isEmpty()) {
                board.addWoodBox(extractDataWoodBox(infoWoodBoxes[i]));
            }
        }
        
        String infoWalls[]=sc.nextLine().split(";");
        for(int i=0;i<infoWalls.length;i++) {
            if (!infoWalls[i].isEmpty()) {
                board.addWall(extractDataWall(infoWalls[i]));
            }
        }
        
        board.computeDanger();
    }
    
    public Player extractDataPlayer(String s) {
        String fragInfo[]=s.split("~");
        Position p=extractPosition(fragInfo[0]);
        int idPlayer=Integer.parseInt(fragInfo[1]);
        String namePlayer=fragInfo[2];
        int numberOfBombs=Integer.parseInt(fragInfo[3]);
        int bombCapacity=Integer.parseInt(fragInfo[4]);
        int bombRange=Integer.parseInt(fragInfo[5]);
        boolean isAlive=(fragInfo[6].charAt(0)=='t');
        int score=Integer.parseInt(fragInfo[7]);
        return new Player(p,idPlayer,namePlayer,numberOfBombs,bombCapacity,bombRange,isAlive,score);
        
    }
    
    public Bomb extractDataBomb(String s) {
        String fragInfo[]=s.split("~");
        Position p=extractPosition(fragInfo[0]);
        int timer=Integer.parseInt(fragInfo[1]);
        int idOwner=Integer.parseInt(fragInfo[2]);
        int range=Integer.parseInt(fragInfo[3]);
        return new Bomb(p, timer, idOwner, range);
    }
    
    public Bonus extractDataBonus(String s) {
        String fragInfo[]=s.split("~");
        Position p=extractPosition(fragInfo[0]);
        BonusType t;
        if(fragInfo[1].charAt(0)=='R') {
            t=BonusType.RANGE;
        }
        else {t=BonusType.NUMBER;}
        return new Bonus(p,t);
    }
    
    public Position extractDataWoodBox(String s) {
        return extractPosition(s);
    }
    
    public Position extractDataWall(String s) {
        return extractPosition(s);
    }
    
    public Position extractPosition(String s) {
        int posX=Integer.parseInt(s.split(",")[0]);
        int posY=Integer.parseInt(s.split(",")[1]);
        return new Position(posX,posY);
    }
    

 }