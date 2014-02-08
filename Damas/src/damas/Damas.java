/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Personal
 */
public class Damas extends JFrame implements MouseListener, ActionListener{
    JButton selec;
    //Variables de Graficos
    JButton btnRegistro, btnRendirse, btnNueva;
    JLabel lblGanadas, lblPerdidas, lblEmpatadas, lblContG, lblContP, lblContE;
    JPanel botones;
    JButton tablero[][];
    JPanel PanelTablero;
    
    public Damas(){
        
        setTitle("Chinese Chekers || Estructuras de datos y algoritmos II");
    	setBounds(10,10,1000,700);//Desginacion de dos enteros que determinaran el tamaño de la ventana X y Y(Ancho y largo respectivamente)
    	setResizable(false);//Para que el usuario no pueda modificar el tamaño de la ventana
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//para que el programa se desvanezca del todo de la memoria del computador
    	getContentPane().setBackground(new Color(Integer.parseInt("a8b2b3", 16)));//Cambiar color de fondo del JFrame
    	getContentPane().setLayout(null);
        
        tablero = new JButton[8][8];
        
        PanelTablero = new JPanel(new GridLayout(8,8));
        
        PanelTablero.setBounds(0,0,640,640);
        
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                tablero[i][j]=new JButton();
                tablero[i][j].setActionCommand(""+i+""+j);
                tablero[i][j].setBounds(0, 0, 80, 80);
                boolean a = false;
                if(i%2==0&&j%2!=0){
                    tablero[i][j].setBackground(new Color(Integer.parseInt("a75e2b",16)));
                }else{
                    tablero[i][j].setBackground(new Color(Integer.parseInt("ffc86c",16)));
                }
                if(i==1||i==3||i==5||i==7){
                    if(j==0||j==2||j==4||j==6){
                        tablero[i][j].setBackground(new Color(Integer.parseInt("a75e2b",16)));
                    }
                }
                PanelTablero.add(tablero[i][j]);
                tablero[i][j].addActionListener(this);
            }
        }
        
        getContentPane().add(PanelTablero);
        
        btnRegistro = new JButton("Registro");
        btnRegistro.setBounds(800, 27, 100, 27);
        btnRegistro.addActionListener(this);
        getContentPane().add(btnRegistro);
        
        btnRendirse = new JButton("Rendirse");
        btnRendirse.setBounds(800, 127, 100, 27);
        btnRendirse.addActionListener(this);
        getContentPane().add(btnRendirse);
        
        btnNueva = new JButton("Otra partida");
        btnNueva.setBounds(800, 227, 100, 27);
        btnNueva.addActionListener(this);
        getContentPane().add(btnNueva);
        
        lblGanadas = new JLabel();
        lblGanadas.setBounds(800, 327, 100, 27);
        
        lblEmpatadas = new JLabel();
        lblEmpatadas.setBounds(800, 327, 100, 27);
        
        lblPerdidas = new JLabel();
        lblPerdidas.setBounds(800, 327, 100, 27);
        
        setVisible(true);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Damas ínterfaz = new Damas();
    }
}
