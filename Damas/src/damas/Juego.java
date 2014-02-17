/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package damas;

import javax.swing.JButton;

/**
 *
 * @author jonathaneidelman & Carlos Sanchez
 */
public class Juego {
    
    public static int matriz[][];
    public static int blanca, negra, Rblanca, Rnegra;
    JButton tablero[][];
    public Juego(JButton tablero[][]) {
        matriz = new int [8][8];
        blanca = 1;
        Rblanca = 2;
        negra = 3;
        Rnegra = 4;
        
        matriz = new int[8][8];
        this.tablero = tablero;
        System.out.println(tablero[0][1].getIcon());
        sincronizarMat();
    }
    
    public void sincronizarMat(){
        String text;
        for(int i=0; i <= 7; i++){
            for(int j=0; j<=7; j++){
                if(tablero[i][j].getIcon()!=null){
                    text = tablero[i][j].getIcon().toString();
                    if(text.equals("src//images//fNegra.png")){
                        matriz[i][j] = 3;
                    }
                    else if(text.equals("src//images//fNegraR.png")){
                        matriz[i][j] = 4;
                    }
                    else if(text.equals("src//images//fBlanca.png")){
                        matriz[i][j] = 1;
                    }
                    else if(text.equals("src//images//fBlancaR.png")){
                        matriz[i][j] = 2;
                    }
                }
            }
        }
        for(int i=0; i <= 7; i++){
            for(int j=0; j<=7; j++){
                System.out.print(matriz[i][j]);
            }
            System.out.println("");
        }
    }
    public void traducirMatriz(){
        
    }
    public boolean validarJugada (int tipo, String inicial, String llegada) throws Exception{
       /*
        Esta es la lista de todas las posibles jugadas de las blancas normales:
        Movida sencilla a la izquierda o derecha y a arriba, comer hacia izquierda o derecha, 
        comer consecutivamente.

        Esta es la lista de todas las posibles jugadas de las negras normales:
        mover sencillo a la izquierda y derecha y a abajo, comer hacia izquierda o derecha,
        comer consecutivamente.

        Esta es la lista de todas las posibles jugadas para una ficha reina (independiente de si es
        blanca o negra):

        mover hacia arriba o hacia abajo y a izquierda o derecha, comer en cualquer direccion,
        comer consecutivamente.
       */ 
       int xi = Integer.parseInt(inicial.charAt(0) + "");
       int yi = Integer.parseInt(inicial.charAt(1) + "");
       int xf = Integer.parseInt(llegada.charAt(0) + "");
       int yf = Integer.parseInt(llegada.charAt(1) + "");
       boolean valida = true;
       if(tipo == 1){ //en caso de que la ficha sea blanca (esta en la parte de abajo del tablero.)
        if(yf <= yi){ // ojo antes era menor o igual
            valida = false; // las fichas blancas normales solo se pueden mover hacia adelante.
            throw new Exception("La ficha solo se puede mover hacia adelante, pues no es una Reina.");
        }
        if(xf == xi){
            valida = false; // las fichas solo se pueden mover en diagonal.
            throw new Exception("Debe mover la ficha tambien en las columnas, la jugada debe ser en diagonal.");

        }else if(Math.abs(xf - xi) == 1 && yf - yi == 1){ //si movio una coordenada en x y una en y. ojo decia -1
            if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2 || matriz[xf][yf] == 3 || matriz[xf][yf] == 4){
                valida = false;
                throw new Exception("La posición de destino esta ocupada, mueva a otra coordenada.");
            }else if(matriz[xf][yf] == 0){
                matriz[xi][yi] = 0; // hizo una movida sencilla a izquierda o a derecha.
                matriz[xf][yf] = 1;
            }
        }else{
             valida = false;
            throw new Exception("Debe mover hacia la izquierda o derecha y hacia adelante unicamente.");
            
        }
        if(Math.abs(xf - xi) == 2 && yf - yi == -2){ // si movio dos en y dos en x, hay que revisar si comio algo.
            if(matriz[xf - 1][yf - 1] == 3 || matriz[xf - 1][yf - 1] == 4){
                if(matriz[xf - 1][yf - 1] != 1 && matriz[xf - 1][yf - 1] != 2){
                
                //revisar bien en que coordenada es en la que tiene que borrar!!!
                matriz[xf][yf] = 1;
                matriz[xi][yi] = 0;
                matriz[xf - 1][yf - 1] = 0; // en el espacio intermedio se borra la ficha que se comio.
                System.out.println("Felicidades, se ha comido una ficha enemiga!");
                System.out.println("Puede efectuar otra jugada con esa ficha,"
                + "siempre y cuando sea para comer.");
                //hay que cranearse como hacer esta parte porque por ahora no se me ocurre.
            }else{
                valida = false;
               throw new Exception("No pude hacer saltos entre fichas propias.");
               
            }
            }
        }else{
             valida = false;
            throw new Exception("Las fichas sencillas solo pueden mover hacia adelante y hacia un lado");
            
        }

        if(Math.abs(xf - xi) != Math.abs(yf - yi)){
            valida = false;
            throw new Exception("La coordenada de llegada no es valida.");
        }

       }

       // para las fichas negras sencillas.

       if(tipo == 3){
        if(xf - xi == 0){
            valida = false;
            throw new Exception("La ficha se debe mover hacia los lados.");
        }
        if(yf - yi <= 0){
            valida = false;
            throw new Exception("La ficha se tiene que mover hacia abajo.");
        }
        if(Math.abs(xf - xi) == 1 && yf - yi == 1){
            if(matriz[xf][yf] == 1 || matriz[xf][yf]==2 || matriz[xf][yf]==3 || matriz[xf][yf]==4){
                valida = false;
                throw new Exception("La coordenada de llegada esta ocupada.");
            }else if(matriz[xf][yf]==0){
                matriz[xf][yf] = 3;
                matriz[xi][yi] = 0;
            }
        }
        if(Math.abs(xf - xi) == 2 && yf - yi == 2){
            if(matriz[xf-1][yf-1]==1 || matriz[xf-1][yf-1]==2){
                //revisar en que coordernada es en la que se tiene que borrar.
                matriz[xi][yi] = 0;
                matriz[xf][yf] = 3;
                matriz[xf - 1][yf - 1] = 0;
                System.out.println("Felicidades, se ha comida una ficha rival.");
                System.out.println("Puede hacer otra jugada con esta ficha.");
                //cranearse como hacer esto.
            }else{
                valida = false;
                throw new Exception("La jugada no es valida, hay una ficha de su equipo alli, "
                        + "o no hay ninguna ficha y no se pueden hacer saltos dobles.");
            }
        }
        if(Math.abs(xf - xi) != yf - yi){
            throw new Exception("La jugada no es valida por su coordenada de llegada.");
        }
       }
    System.out.println("!!!!!!!!!!!!!!!!!!!");
       for(int i=0; i <= 7; i++){
            for(int j=0; j<=7; j++){
                System.out.print(matriz[i][j]);
            }
            System.out.println("");
        }
       System.out.println("!!!!!!!!!!!!!!!!!!!");
       return valida;
    }
    public void calcularJugada(int mat[][]){
        
    }
    public int[][] getMat(){
        return matriz;
    }
}
