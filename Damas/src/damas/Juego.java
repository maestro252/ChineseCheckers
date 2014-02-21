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
    public static int turno = 0;
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
       boolean valida = true;
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
                turno++;
                if(xf == 0){
                    matriz[xf][yf] = 2;
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
                turno++;
                if(xf == 0){
                    matriz[xf][yf] = 2;
                }
                System.out.println("Felicidades, se ha comido una ficha enemiga!");
                System.out.println("Puede efectuar otra jugada con esa ficha,"
                + "siempre y cuando sea para comer.");
                
                }else{
                    valida = false;
                   throw new Exception("No pude hacer saltos entre fichas propias o comidas dobles.");

                }
            }
        }/*else{
             valida = false;
            throw new Exception("Las fichas sencillas solo pueden mover hacia adelante y hacia un lado.");
            
        }*/ // OJO A VER QUE PASA SIN ESTE ELSE!!!

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
                turno++;
                matriz[xf][yf] = 2;
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
            }
            
            }
        matriz[xf][yf] = 2;
        matriz[xi][yi] = 0;
        turno++;
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
                System.out.println("Entre!!!");
                matriz[xf][yf] = 3;
                matriz[xi][yi] = 0;
                turno++;
                if(xf == 7){
                    matriz[xf][yf] = 4;
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
                turno++;
                if(xf == 7){
                    matriz[xf][yf] = 4;
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
                turno++;
                matriz[xf][yf] = 4;
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
            }
            
            }
        matriz[xf][yf] = 4;
        matriz[xi][yi] = 0;
        turno++;
       }
       
      return valida;
    }
    public void calcularJugada(int mat[][]){
        
    }
    public int[][] getMat(){
        return matriz;
    }
}
