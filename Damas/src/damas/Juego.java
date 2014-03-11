/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package damas;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author jonathaneidelman & Carlos Sanchez
 */
public class Juego {
    public int numBlancas = 12;
    public int numNegras = 12;
    public boolean a = false;
    int c, d;
    public static int matriz[][];
    public int turno = 0;
    public static int blanca, negra, Rblanca, Rnegra;
    public String s = "";
    JButton tablero[][];
    public boolean reinaInteligente[][];
    public Juego(JButton tablero[][]) {
        matriz = new int [8][8];
        blanca = 1;
        Rblanca = 2;
        negra = 3;
        Rnegra = 4;
        reinaInteligente = new boolean [4][2];
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
    
    public boolean validarJugada (int tipo, String inicial, String llegada) throws Exception{
        
        boolean atrancado = atrancado(turno);
        if(!atrancado){
            String g;
            if(turno % 2 == 0){
                g = "Gana la máquina.";
            }else{
                g= "Gana el humano.";
            }
            JOptionPane.showMessageDialog(Damas.interfaz, "Victoria!!!\n" + g,
                                          "No hay jugadas validas!", JOptionPane.WARNING_MESSAGE);
            Damas.interfaz.dispose();
            Damas.interfaz = new Damas();
            s = "";
            Damas.jugadas.setText("");
            return true;
        }
        boolean valida = true;
        a = false;
        c = 0;
        d = c;
        if(turno % 2 == 0){
            if(tipo == 3 || tipo == 4){
                valida = false;
                throw new Exception("Es el turno de las blancas.");
            }
        }else if(turno % 2 == 1){
            if(tipo == 1 || tipo == 2){
                valida = false;
                throw new Exception("Es el turno de las negras.");
            }
        }
        int xi = Integer.parseInt(inicial.charAt(0) + "");
        int yi = Integer.parseInt(inicial.charAt(1) + "");
        int xf = Integer.parseInt(llegada.charAt(0) + "");
        int yf = Integer.parseInt(llegada.charAt(1) + "");
        
        if(tipo == 1){ //en caso de que la ficha sea blanca (esta en la parte de abajo del tablero.)
            if(Math.abs(xf - xi) == 0 && Math.abs(yf - yi) == 0){
                valida = false;
                throw new Exception("No se puede quedar quieta.");
            }
            if(xf > xi){ // ojo antes era menor o igual
                valida = false; // las fichas blancas normales solo se pueden mover hacia adelante.
                throw new Exception("La ficha solo se puede mover hacia adelante, pues no es una Reina.");
            }
            if(yf == yi){
                valida = false; // las fichas solo se pueden mover en diagonal.
                throw new Exception("La jugada tiene que ser en diagonal!");
                
            }else if(xf - xi == -1 && Math.abs(yf - yi) == 1){ //si movio una coordenada en x y una en y. ojo decia -1
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La posición de destino esta ocupada, mueva a otra coordenada valida.");
                }else if(matriz[xf][yf] == 0){
                    matriz[xi][yi] = 0; // hizo una movida sencilla a izquierda o a derecha.
                    matriz[xf][yf] = 1;
                    s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                    Damas.jugadas.setText(s);
                    turno++;
                    //inteligencia();
                    if(xf == 0){
                        matriz[xf][yf] = 2;
                        s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " R" + "\n";
                        Damas.jugadas.setText(s);
                        
                    }
                }
            }else if(Math.abs(xf - xi) != Math.abs(yf - yi)){
                valida = false;
                throw new Exception("Debe mover una o dos unidades en ambas coordenadas.");
                
            }
            if(xf - xi == -2 && Math.abs(yf - yi) == 2){ // si movio dos en y dos en x, hay que revisar si comio algo.
                int yaux;
                if(yf - yi > 0){
                    yaux = yf - 1;
                }else{
                    yaux = yf + 1;
                }
                if(matriz[xf + 1][yaux] == 3 || matriz[xf + 1][yaux] == 4){
                    if((matriz[xf + 1][yaux] != 1 && matriz[xf + 1][yaux] != 2) && matriz[xf][yf] == 0){
                        matriz[xf][yf] = 1;
                        matriz[xi][yi] = 0;
                        matriz[xf + 1][yaux] = 0; // en el espacio intermedio se borra la ficha que se comio.
                        //aqui debe ir la parte que permita volver a jugar en caso de que tenga mas por comer.
                        if(((xf-2 >= 0 && yf-2 >= 0 && yf+2 <= 7))&&((matriz[xf-1][yf+1]!=0) && (matriz[xf-2][yf+2]==0)||((matriz[xf-1][yf-1]!=0) && (matriz[xf-2][yf-2]==0)))){
                            //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
                        }else{
                            turno++;
                        }
                        numNegras--;
                        if(numNegras == 0){
                            JOptionPane.showMessageDialog(Damas.interfaz, "VICTORIA!!! gana el humano.",
                                                          "Felicidades!!!", JOptionPane.WARNING_MESSAGE);
                            Damas.interfaz.dispose();
                            Damas.interfaz = new Damas();
                            s = "";
                            Damas.jugadas.setText("");
                            return true;
                        }
                        if(xf == 0){
                            matriz[xf][yf] = 2;
                            s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " ||| " + (xf+1) + "," + yaux +  " C, R" + "\n";
                            Damas.jugadas.setText(s);
                        }else{
                            s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " ||| " + (xf+1) + "," + yaux +  " C" + "\n";
                            Damas.jugadas.setText(s);
                        }
                        System.out.println("Felicidades, se ha comido una ficha enemiga!");
                        System.out.println("Puede efectuar otra jugada con esa ficha,"
                                           + "siempre y cuando sea para comer.");
                        
                    }else{
                        valida = false;
                        throw new Exception("No pude hacer saltos entre fichas propias o comidas dobles.");
                        
                    }
                }
            }
            
            if(Math.abs(xf - xi) != Math.abs(yf - yi)){
                valida = false;
                throw new Exception("La coordenada de llegada no es valida.");
            }
            
        }
        
        if(tipo == 2){ //en caso de que la ficha sea blanca (esta en la parte de abajo del tablero.)
            if(Math.abs(xf - xi) == 0 && Math.abs(yf - yi) == 0){
                valida = false;
                throw new Exception("Debe moverse.");
            }
            if(yf == yi){
                valida = false; // las fichas solo se pueden mover en diagonal.
                throw new Exception("La jugada tiene que ser en diagonal!");
                
            }else if(Math.abs(xf - xi) == 1 && Math.abs(yf - yi) == 1){ //si movio una coordenada en x y una en y. ojo decia -1
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La posición de destino esta ocupada, mueva a otra coordenada valida.");
                }else if(matriz[xf][yf] == 0){
                    matriz[xi][yi] = 0; // hizo una movida sencilla a izquierda o a derecha.
                    matriz[xf][yf] = 2;
                    s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                    Damas.jugadas.setText(s);
                }
            }else if(Math.abs(xf - xi) != Math.abs(yf - yi)){
                valida = false;
                throw new Exception("Debe mover las mismas unidades en ambas coordenadas.");
                
            }
            if(Math.abs(xf - xi) >= 2 && Math.abs(yf - yi) >= 2){
                int yaux;
                boolean derecha = false;
                boolean izquierda = false;
                boolean arriba = false;
                boolean abajo = false;
                if(yf - yi > 0){
                    yaux = yf - 1;
                    derecha = true;
                }else{
                    yaux = yf + 1;
                    izquierda = true;
                }
                int xaux;
                if(xf - xi > 0){
                    xaux = xf - 1;
                    abajo = true;
                }else{
                    xaux = xf + 1;
                    arriba = true;
                }
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La posicion de llegada esta ocupada.");
                }
                if(Math.abs(xf - xi) == 0 && Math.abs(yf - yi) == 0){
                    valida = false;
                    throw new Exception("Debe moverse!!!");
                }
                int i = xi;
                int j = yi;
                int dx = (xf - xi)/Math.abs(xf - xi);
                int dy = (yf - yi) / Math.abs(yf - yi);
                i = i + dx;
                j = j + dy;
                int cont = 0;
                int posx = 0;
                int posy = 0;
                while(i != xf){
                    if(matriz[i][j] == 1 || matriz[i][j] == 2){
                        valida = false;
                        throw new Exception("No puede pasar por sobre fichas propias.");
                    }
                    if(matriz[i][j] == 3 || matriz[i][j] == 4){
                        cont++;
                        posx = i;
                        posy = j;
                    }
                    i = i + dx;
                    j = j + dy;
                }
                if(cont >= 2){
                    valida = false;
                    throw new Exception("No puede comer mas de una ficha contraria.");
                }else if(cont == 1){
                    matriz[posx][posy] = 0;
                    a = true;
                    c = posx;
                    d = posy;
                    numNegras--;
                }
                
            }
            
            if(numNegras == 0){
                JOptionPane.showMessageDialog(Damas.interfaz, "VICTORIA!!! gana el humano.",
                                              "Felicidades!!!", JOptionPane.WARNING_MESSAGE);
                Damas.interfaz.dispose();
                Damas.interfaz = new Damas();
                s = "";
                Damas.jugadas.setText("");
                return true;
            }
            matriz[xf][yf] = 2;
            matriz[xi][yi] = 0;
            if(((xf-2 >= 0 && xf+2<=7 && yf-2 >= 0 && yf+2<=7))&&
               ((matriz[xf-1][yf+1]!=0) && (matriz[xf-2][yf+2]==0)||((matriz[xf-1][yf-1]!=0) && (matriz[xf-2][yf-2]==0)))){
                //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
            }else if (((xf+2 >= 0 && xf+2<=7 && yf-2 >= 0 && yf+2<=7))&&(((matriz[xf+1][yf+1]!=0) && (matriz[xf+2][yf+2]==0))||((matriz[xf+1][yf-1]!=0) && (matriz[xf+2][yf-2]==0)))){
                //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
            }
            else{
                turno++;
                //inteligencia();
            }
            if(a){
                s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " ||| " + c + "," + d +  " C" + "\n";
                Damas.jugadas.setText(s);
            }else{
                s += "Humano:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                Damas.jugadas.setText(s);
            }
            
        }
        
        
        if(tipo == 3){
            if(Math.abs(xf - xi) == 0 && Math.abs(yf - yi) == 0){
                valida = false;
                throw new Exception("Debe moverse.");
            }
            if(xf - xi == 0){
                valida = false;
                throw new Exception("La ficha solo se debe mover en diagonal hacia abajo.");
            }
            if(yf - yi == 0){
                valida = false;
                throw new Exception("La ficha se debe mover en diagonal hacia abajo.");
            }
            if(xf - xi == 1 && Math.abs(yf - yi) == 1){
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La coordenada de llegada esta ocupada.");
                }else if(matriz[xf][yf]== 0){
                    matriz[xf][yf] = 3;
                    matriz[xi][yi] = 0;
                    s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                    Damas.jugadas.setText(s);
                    turno++;
                    if(xf == 7){
                        matriz[xf][yf] = 4;
                        s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " R" + "\n";
                        Damas.jugadas.setText(s);
                    }
                }
            }
            if(xf - xi == 2 && Math.abs(yf - yi) == 2){
                int yaux;
                if(yf - yi < 0){
                    yaux = yf + 1;
                }else{
                    yaux = yf - 1;
                }
                if((matriz[xf-1][yaux]==1 || matriz[xf-1][yaux]==2) && matriz[xf][yf] == 0){
                    //revisar en que coordernada es en la que se tiene que borrar.
                    matriz[xi][yi] = 0;
                    matriz[xf][yf] = 3;
                    matriz[xf - 1][yaux] = 0;
                    if(((xf+2 <= 7 && yf+-2 >=0 &&yf+2 <= 7))&&((matriz[xf+1][yf+1]!=0) && (matriz[xf+2][yf+2]==0)||((matriz[xf+1][yf-1]!=0) && (matriz[xf+2][yf-2]==0)))){
                        //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
                    }else{
                        turno++;
                    }
                    numBlancas--;
                    if(numBlancas == 0){
                        JOptionPane.showMessageDialog(Damas.interfaz, "VICTORIA!!! gana la máquina!",
                                                      "Has perdido", JOptionPane.WARNING_MESSAGE);
                        Damas.interfaz.dispose();
                        Damas.interfaz = new Damas();
                        s = "";
                        Damas.jugadas.setText("");
                        return true;
                    }
                    if(xf == 7){
                        matriz[xf][yf] = 4;
                        s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " ||| " + (xf+1) + "," + yaux +  " C, R" + "\n";
                        Damas.jugadas.setText(s);
                    }else{
                        s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) +  " ||| " + (xf+1) + "," + yaux + " C" + "\n";
                        Damas.jugadas.setText(s);
                    }
                    System.out.println("Felicidades, se ha comida una ficha rival.");
                    System.out.println("Puede hacer otra jugada con esta ficha.");
                    //cranearse como hacer esto.
                }else{
                    valida = false;
                    throw new Exception("La jugada no es valida, hay una ficha de su equipo alli, "
                                        + "o no hay ninguna ficha y no se pueden hacer saltos dobles o comidas dobles.");
                }
            }
            if(Math.abs(xf - xi) != Math.abs(yf - yi)){
                valida = false;
                throw new Exception("La jugada no es valida por su coordenada de llegada.");
            }
        }
        
        if(tipo == 4){
            if(Math.abs(xf - xi) == 0 && Math.abs(yf - yi) == 0){
                valida = false;
                throw new Exception("Debe moverse.");
            }
            if(yf == yi){
                valida = false; // las fichas solo se pueden mover en diagonal.
                throw new Exception("La jugada tiene que ser en diagonal!");
                
            }else if(Math.abs(xf - xi) == 1 && Math.abs(yf - yi) == 1){ //si movio una coordenada en x y una en y. ojo decia -1
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La posición de destino esta ocupada, mueva a otra coordenada valida.");
                }else if(matriz[xf][yf] == 0){
                    matriz[xi][yi] = 0; // hizo una movida sencilla a izquierda o a derecha.
                    matriz[xf][yf] = 4;
                    s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                    Damas.jugadas.setText(s);
                    
                }
            }else if(Math.abs(xf - xi) != Math.abs(yf - yi)){
                valida = false;
                throw new Exception("Debe mover las mismas unidades en ambas coordenadas.");
                
            }
            if(Math.abs(xf - xi) >= 2 && Math.abs(yf - yi) >= 2){
                int yaux;
                boolean derecha = false;
                boolean izquierda = false;
                boolean arriba = false;
                boolean abajo = false;
                if(yf - yi > 0){
                    yaux = yf - 1;
                    derecha = true;
                }else{
                    yaux = yf + 1;
                    izquierda = true;
                }
                int xaux;
                if(xf - xi > 0){
                    xaux = xf - 1;
                    abajo = true;
                }else{
                    xaux = xf + 1;
                    arriba = true;
                }
                if(matriz[xf][yf] != 0){
                    valida = false;
                    throw new Exception("La posicion de llegada esta ocupada.");
                }
                if(Math.abs(xf - xi) == 0 || Math.abs(yf - yi) == 0){
                    valida = false;
                    throw new Exception("Debe moverse!!!");
                }
                int i = xi;
                int j = yi;
                int dx = (xf - xi)/Math.abs(xf - xi);
                int dy = (yf - yi) / Math.abs(yf - yi);
                i = i + dx;
                j = j + dy;
                int cont = 0;
                int posx = 0;
                int posy = 0;
                while(i != xf){
                    if(matriz[i][j] == 3 || matriz[i][j] == 4){
                        valida = false;
                        throw new Exception("No puede pasar por sobre fichas propias.");
                    }
                    if(matriz[i][j] == 1 || matriz[i][j] == 2){
                        cont++;
                        posx = i;
                        posy = j;
                    }
                    i = i + dx;
                    j = j + dy;
                }
                if(cont >= 2){
                    valida = false;
                    throw new Exception("No puede comer mas de una ficha contraria.");
                }else if(cont == 1){
                    matriz[posx][posy] = 0;
                    a = true;
                    c = posx;
                    d = posy;
                    numBlancas--;
                }
                
            }
            matriz[xf][yf] = 4;
            matriz[xi][yi] = 0;
            if(numBlancas == 0){
                JOptionPane.showMessageDialog(Damas.interfaz, "VICTORIA!!! gana la máquina!",
                                              "Has perdido", JOptionPane.WARNING_MESSAGE);
                Damas.interfaz.dispose();
                Damas.interfaz = new Damas();
                s = "";
                Damas.jugadas.setText("");
                return true;
            }
            if(a){
                s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + " ||| " + c + "," + d +  " C" + "\n";
                Damas.jugadas.setText(s);
            }else{
                s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
                Damas.jugadas.setText(s);
            }
            if(((xf-2 >= 0 && xf+2<=7 && yf-2 >= 0 && yf+2<=7))&&
               ((matriz[xf-1][yf+1]!=0) && (matriz[xf-2][yf+2]==0)||((matriz[xf-1][yf-1]!=0) && (matriz[xf-2][yf-2]==0)))){
                //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
            }else if (((xf+2 >= 0 && xf+2<=7 && yf-2 >= 0 && yf+2<=7))&&(((matriz[xf+1][yf+1]!=0) && (matriz[xf+2][yf+2]==0))||((matriz[xf+1][yf-1]!=0) && (matriz[xf+2][yf-2]==0)))){
                //Al efectuar validaciones la ficha tiene la posibilidad de mover doble
            }
            else{
                turno++;
            }
            s += "Máquina:" + (-xi+8) + "," + (yi+1) + " a " + (-xf+8) + "," + (yf + 1) + "\n";
            Damas.jugadas.setText(s);
        }
        return valida;
    }
    public void calcularJugada(int mat[][]){
        
    }
    public int[][] getMat(){
        return matriz;
    }
    public boolean atrancado(int turno){
        boolean puede = false;
        if(turno % 2 == 0){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(matriz[i][j] == 1 || matriz[i][j] == 2){
                        if(matriz[i][j] == 1){
                            puede = movida(1, i, j, -1, 1,false);
                            if(puede){
                                return true;
                            }
                            puede = movida(1, i, j, -1, -1,false);
                            if(puede){
                                return true;
                            }
                        }else if(matriz[i][j] == 2){
                            puede = movida(2, i, j, 1, 1,false);
                            if(puede){
                                return true;
                            }
                            puede = movida(2, i, j, 1, -1,false);
                            if(puede){
                                return true;
                            }
                        }
                    }
                }
            }
        }else{
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(matriz[i][j] == 3 || matriz[i][j] == 4){
                        puede = movida(3, i, j, 1, 1,false);
                        if(puede){
                            return true;
                        }
                        puede = movida(3, i, j, 1, -1,false);
                        if(puede){
                            return true;
                        }
                    }
                    
                    if(matriz[i][j] == 4){
                        puede = movida(4, i, j, -1, 1,false);
                        if(puede){
                            return true;
                        }
                        puede = movida(4, i, j, -1, -1, false);
                        if(puede){
                            return true;
                        }
                    }
                }
            }
            
        }
        
        return puede;
    }
    public boolean movida(int tipo, int i, int j, int di, int dj, boolean x){
        boolean puede = false;
        boolean imprimio = false;
        if((i + di >= 0 && i + di <= 7) && (j + dj >= 0 && j + dj <= 7)){
            if(matriz[i + di][j + dj] == 0){
                puede = true;
                if(x){
                    matriz[i][j] = 0;
                    matriz[i + di][j + dj] = tipo;
                    turno++;
                    if(i + di == 7 ){
                        matriz[i + di][j + dj] = 4;
                        s += "Máquina:" + (-i+8) + "," + (j+1) + " a " + (-(i + di)+8) + "," + (j + dj + 1) + " R" +  "\n" ;
                        Damas.jugadas.setText(s);
                        imprimio = true;
                    }
                    if(!imprimio){
                        s += "Máquina:" + (-i+8) + "," + (j+1) + " a " + (-(i + di)+8) + "," + (j + dj + 1) + "\n";
                        Damas.jugadas.setText(s);
                    }
                }
                return puede;
            }else{
                if(tipo == 1 || tipo == 2){
                    if(matriz[i + di][j + dj] == 3 || matriz[i + di][j + dj] == 4){
                        if(((i + 2 * di >= 0 && i + 2 * di <= 7) && (j + 2*dj >= 0 && j + 2*dj <= 7)) && matriz[i + 2*di][j + 2*dj] == 0){
                            puede = true;
                            return puede;
                        }
                    }
                }else if(tipo == 3 || tipo == 4){
                    if(matriz[i + di][j + dj] == 1 || matriz[i + di][j + dj] == 2){
                        if(((i + 2 * di >= 0 && i + 2 * di <= 7) && (j + 2*dj >= 0 && j + 2*dj <= 7)) && matriz[i + 2*di][j + 2*dj] == 0){
                            puede = true;
                            if(x){
                                matriz[i][j] = 0;
                                matriz[i + 2*di][j + 2*dj] = tipo;
                                matriz[i+di][j+dj] = 0;
                                turno++;
                                if(i + 2*di == 7 ){
                                    matriz[i + 2*di][j + 2*dj] = 4;
                                    s += "Máquina:" + (-i+8) + "," + (j+1) + " a " + (-(i + 2*di)+8) + "," + (j + 2*dj + 1) + " ||| " + (-(i + di) + 8) + "," + (j + dj + 1) + " C, R" +  "\n";
                                    Damas.jugadas.setText(s);
                                    imprimio = true;
                                }
                                if(!imprimio){
                                    s += "Máquina:" + (-i+8) + "," + (j+1) + " a " + (-(i + 2*di)+8) + "," + (j + 2*dj + 1) + " ||| " + (-(i + di) + 8) + "," + (j + dj + 1) + " C" + "\n";
                                    Damas.jugadas.setText(s);
                                }
                            }
                            return puede;
                        }
                    }
                }
            }
        }
        return puede;
    }
    public void inteligencia(){
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(turno % 2 == 0){
            return;
        }
        if(turno == 1){
            int primero[] = {1,3,5,7};
            int x = (int)Math.round(Math.random() * 3);
            x = primero[x];
            int y = 1, f = -1;
            y = (int)Math.random()*(y-f)+ f;
            //realizar movida.
            matriz[2][x] = 0;
            matriz[3][x+y] = 3;
            turno++;
            s += "Máquina:" + "6" + "," + (x+1) + " a " + "5" + "," + (x + y + 1) + "\n";
            Damas.jugadas.setText(s);
            
            
        }else{//aqui va toda la logica del resto de movimientos de la maquina.
            //ensayar con llamados con booleans por defecto en los metodos de atascamiento.
            boolean atrancado = atrancado(turno);
            if(!atrancado){
                String g;
                if(turno % 2 == 0){
                    g = "Gana la máquina.";
                }else{
                    g= "Gana el humano.";
                }
                JOptionPane.showMessageDialog(Damas.interfaz, "Victoria!!!\n" + g,
                                              "No hay jugadas validas!", JOptionPane.WARNING_MESSAGE);
                Damas.interfaz.dispose();
                Damas.interfaz = new Damas();
                s = "";
                Damas.jugadas.setText("");
                return;
            }
            String arr[] = new String [numNegras];
            int pos = 0;
            boolean hayReina = false;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(matriz[i][j] == 3 || matriz[i][j] == 4){
                        arr[pos] = "" + i + j;
                        pos++;
                        if(matriz[i][j] == 4){
                            hayReina = true;
                            boolean movio = false;
                            analizarNegra(i,j);
                            if(reinaInteligente[0][0] == true && !movio){
                                moverDiagonal1(i,j);
                                reinaInteligente[0][0] = false;
                                movio = true;
                            }
                            if(reinaInteligente[1][0] == true && !movio){
                                moverDiagonal2(i,j);
                                reinaInteligente[1][0] = false;
                                movio = true;
                            }
                            if(reinaInteligente[2][0] == true && !movio){
                                moverDiagonal3(i,j);
                                reinaInteligente[2][0] = false;
                                movio = true;
                            }
                            if(reinaInteligente[3][0] == true && !movio){
                                moverDiagonal4(i,j);
                                reinaInteligente[3][0] = false;
                                movio = true;
                            }
                            //si no pudo comer.
                            if(reinaInteligente[0][1] == true && !movio){
                                moverDiagonal1(i,j);
                                reinaInteligente[0][1] = false;
                                movio = true;
                            }
                            if(reinaInteligente[1][1] == true && !movio){
                                moverDiagonal2(i,j);
                                reinaInteligente[1][1] = false;
                                movio = true;
                            }
                            if(reinaInteligente[2][1] == true && !movio){
                                moverDiagonal3(i,j);
                                reinaInteligente[2][1] = false;
                                movio = true;
                            }
                            if(reinaInteligente[3][1] == true && !movio){
                                moverDiagonal4(i,j);
                                reinaInteligente[3][1] = false;
                                movio = true;
                            }
                            if(matriz[i][j] != 4){
                                return;
                            }
                        }
                    }
                }
            }
            pos = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(matriz[i][j] > 2){
                        arr[pos] = "" + i + j;
                        pos++;
                    }
                }
                
            }
            
            int posx, posy, azar;
            azar = ((int)Math.round(Math.random()*(numNegras - 1)));
            System.out.println("Azar es : " + azar);
            String ubicacion = arr[azar];
            posx = Integer.parseInt(ubicacion.charAt(0) + "");
            posy = Integer.parseInt(ubicacion.charAt(1) + "");
            int tipito = matriz[posx][posy];
            boolean movi = false;
            while(!movi){
                if(tipito != 4){
                    if(movida(tipito, posx, posy, 1, 1, false )){ // se deben hacer las preguntas de si puede comer
                        // porque de poder es mejor que lo haga
                        // en el else va la movida sencilla.
                        //de no tener validas, se escoge otra ficha al azar.
                        movida(tipito, posx, posy, 1, 1, true);
                        movi = true;
                        
                        
                    }else if(movida(tipito, posx, posy, 1, -1, false )){
                        
                        movida(tipito, posx, posy, 1, -1, true);
                        movi = true;
                        
                    }
                    azar = ((int)Math.round(Math.random()*(numNegras - 1)));
                    System.out.println("Azar es : " + azar);
                    ubicacion = arr[azar];
                    posx = Integer.parseInt(ubicacion.charAt(0) + "");
                    posy = Integer.parseInt(ubicacion.charAt(1) + "");
                    tipito = matriz[posx][posy];
                }else{//Aqui esta el bollo.!!!
                    azar = ((int)Math.round(Math.random()*(numNegras - 1)));
                    System.out.println("Azar es : " + azar);
                    ubicacion = arr[azar];
                    posx = Integer.parseInt(ubicacion.charAt(0) + "");
                    posy = Integer.parseInt(ubicacion.charAt(1) + "");
                    tipito = matriz[posx][posy];
                }
            }
            
            
        }
    }
    public boolean moverNegra(int x, int y){
        //hacia abajo a la derecha.
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx = 1;
        int dy = 1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0, ycomer = 0;
        if(xf <= 7 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - 1][yf - 1] = 4;
                                matriz[xcomer][ycomer] = 0;
                                //falta turno++ por el caso en que pueda volver a comer.
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - 1][yf - 1] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf++;
                yf++;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf - 1][yf - 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        //hacia arriba a la izquierda
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx = -1;
        dy = -1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf >= 0 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf--;
                yf--;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf + 1][yf + 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        //hacia abajo a la izquierda
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx =  1;
        dy = -1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf <= 7 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf - 1][yf + 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        //hacia arriba a la derecha
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx = -1;
        dy =  1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf >= 0 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf + 1][yf - 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        
        return false;
    }
    
    public boolean moverDiagonal1(int x, int y){
        //hacia abajo a la derecha.
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx = 1;
        int dy = 1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0, ycomer = 0;
        if(xf <= 7 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - 1][yf - 1] = 4;
                                matriz[xcomer][ycomer] = 0;
                                //falta turno++ por el caso en que pueda volver a comer.
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - 1][yf - 1] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf++;
                yf++;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf - 1][yf - 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        return false;
    }
    
    
    public boolean moverDiagonal2(int x, int y){
        //hacia arriba a la izquierda
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx = -1;
        int dy = -1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0;
        int ycomer = 0;
        if(xf >= 0 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf--;
                yf--;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf + 1][yf + 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        return false;
    }
    
    public boolean moverDiagonal3(int x, int y){
        //hacia abajo a la izquierda
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx =  1;
        int dy = -1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0;
        int ycomer = 0;
        if(xf <= 7 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf - 1][yf + 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        return false;
    }
    
    
    public boolean moverDiagonal4(int x, int y){
        //hacia arriba a la derecha
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx = -1;
        int dy =  1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0;
        int ycomer = 0;
        if(xf >= 0 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    matriz[x][y] = 0;
                                    matriz[xf - 2*dx][yf - 2*dy] = 4;
                                    turno ++;
                                    return true;
                                }else{
                                    break;
                                }
                            }else{
                                matriz[x][y] = 0;
                                matriz[xf - dx][yf - dy] = 4;
                                matriz[xcomer][ycomer] = 0;
                                turno++;
                                numBlancas--;
                                //pantallazo si se acaba el juego.
                                return true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            matriz[x][y] = 0;
                            matriz[xf - dx][yf - dy] = 4;
                            turno++;
                            if(xcomer > 0 || ycomer > 0){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                                //JoptionPane
                            }
                            return true;
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                matriz[xcomer][ycomer] = 0;
                                numBlancas--;
                            }
                            matriz[xf - dx][yf - dy] = 4;
                            matriz[x][y] = 0;
                            turno++;
                            return true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                matriz[xcomer][ycomer] = 0;
                numBlancas--;
                //poner el Joption pane si las blancas son == 0
                matriz[x][y] = 0;
                matriz[xf + 1][yf - 1] = 4;
                turno++;
                return true;
            }else if(!comer && n > 0){
                matriz[xf - dx][yf - dy] = 4;
                matriz[x][y] = 0;
                turno++;
                return true;
            }else if(comer && m == 0 && n >= 1){
                matriz[x][y] = 0;
                matriz[xf - 2*dx][yf - 2*dy] = 4;
                turno++;
                return true;
            }
        }
        return false;
    }
    
    
    
    public void analizarNegra(int x, int y){
        //hacia abajo a la derecha.
        int m = 0; //espacios en blanco despues de ficha contraria.
        boolean comer = false;
        int n = 0; //espacios en blanco directamente despues de la ficha en x,y
        int dx = 1;
        int dy = 1;
        int xf = x + dx;
        int yf = y + dy;
        int xcomer = 0, ycomer = 0;
        if(xf <= 7 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    reinaInteligente[0][1] = true;
                                }else{
                                    break;
                                }
                            }else{
                                reinaInteligente[0][0] = true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            reinaInteligente[0][1] = true;
                            if(xcomer > 0 || ycomer > 0){
                                reinaInteligente[0][0] = true;
                            }
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                reinaInteligente[0][0] = true;
                            }
                            reinaInteligente[0][1] = true;
                        }
                    }
                    
                }
                xf++;
                yf++;
            }
            if(comer && m > 0){
                reinaInteligente[0][0] = true;
            }else if(!comer && n > 0){
                reinaInteligente[0][1] = true;
            }else if(comer && m == 0 && n >= 1){
                reinaInteligente[0][0] = true;
            }
        }
        //hacia arriba a la izquierda
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx = -1;
        dy = -1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf >= 0 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    reinaInteligente[1][1] = true;
                                }else{
                                    break;
                                }
                            }else{
                                reinaInteligente[1][0] = true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            reinaInteligente[1][1] = true;
                            if(xcomer > 0 || ycomer > 0){
                                reinaInteligente[1][0] = true;
                            }
                            
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                reinaInteligente[1][0] = true;
                            }
                            reinaInteligente[1][1] = true;
                        }
                    }
                    
                }
                xf--;
                yf--;
            }
            if(comer && m > 0){
                reinaInteligente[1][0] = true;
            }else if(!comer && n > 0){
                reinaInteligente[1][1] = true;
            }else if(comer && m == 0 && n >= 1){
                reinaInteligente[1][1] = true;
            }
        } 
        //hacia abajo a la izquierda
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx =  1;
        dy = -1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf <= 7 && yf >= 0 && matriz[xf][yf] < 3){
            while(xf <= 7 && yf >= 0){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    reinaInteligente[2][1] = true;
                                }else{
                                    break;   
                                }
                            }else{
                                reinaInteligente[2][0] = true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            reinaInteligente[2][1] = true;
                            if(xcomer > 0 || ycomer > 0){
                                reinaInteligente[2][0] = true;
                            }
                            
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                reinaInteligente[2][0] = true;
                            }
                            reinaInteligente[2][1] = true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                reinaInteligente[2][0] = true;
            }else if(!comer && n > 0){
                reinaInteligente[2][1] = true;
            }else if(comer && m == 0 && n >= 1){
                reinaInteligente[2][1] = true;
            }
        }
        //hacia arriba a la derecha
        m = 0; //espacios en blanco despues de ficha contraria.
        comer = false;
        n = 0; //espacios en blanco directamente despues de la ficha en x,y
        dx = -1;
        dy =  1;
        xf = x + dx;
        yf = y + dy;
        xcomer = 0;
        ycomer = 0;
        if(xf >= 0 && yf <= 7 && matriz[xf][yf] < 3){
            while(xf >= 0 && yf <= 7){
                if(matriz[xf][yf] == 0){
                    if(!comer){
                        n++;
                    }else{
                        m++;
                    }
                }else{
                    if(matriz[xf][yf] == 1 || matriz[xf][yf] == 2){
                        if(!comer){
                            comer = true;
                            xcomer = xf;
                            ycomer = yf;
                        }else{
                            if(m == 0){
                                if(n >= 1){
                                    reinaInteligente[3][1] = true;
                                }else{
                                    break;
                                }
                            }else{
                                reinaInteligente[3][0] = true;
                            }
                        }
                    }else{
                        if(n > 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            reinaInteligente[3][1] = true;
                            if(xcomer > 0 || ycomer > 0){
                                reinaInteligente[3][0] = true;
                            }
                            
                        }else if(n == 0 && (matriz[xf][yf] == 3 || matriz[xf][yf] == 4)){
                            break;
                        }
                        if(n > 0 || m > 0){
                            if(comer){
                                reinaInteligente[3][0] = true;
                            }
                            reinaInteligente[3][1] = true;
                        }
                    }
                    
                }
                xf = xf + dx;
                yf = yf + dy;
            }
            if(comer && m > 0){
                reinaInteligente[3][0] = true;
            }else if(!comer && n > 0){
                reinaInteligente[3][1] = true;
            }else if(comer && m == 0 && n >= 1){
                reinaInteligente[3][1] = true;
            }
        }
    }
    
    
    public int getTurno(){
        return turno;
    }
}
