package org.example;// se hicierón diversas clases ya que utilizarón divesos métos para la realiación compleja del código
// Autores: Krystel Hiromy Hernández Guzmán
//          Yenit López Padilla
//          Azucena Eloisa Luis Santiago



import java.util.Scanner;

/*
 * PROYECTO: CALCULADORA ELECTROQUIMICA
 *
 * Este programa permite consultar potenciales estandar, resolver celdas
 * galvanicas y electroliticas, identificar el anodo y el catodo,
 * determinar las reacciones de oxidacion y reduccion, calcular los
 * electrones transferidos y obtener el potencial mediante Nernst.
 *
 * Se utilizan ciclos while, for, condiciones if y bloques try-catch
 * para validar los datos ingresados.
 */

public class Calculadoracorrosion {

    // SCANNER PARA LEER LOS DATOS DEL USUARIO
    private static final Scanner lector = new Scanner(System.in);

    // CONSTANTES DE NERNST A 25 GRADOS CELSIUS
    private static final double CONSTANTE_NERNST = 0.0592;
    private static final double TEMPERATURA = 25.0;

    // TABLA DE POTENCIALES ESTANDAR DE REDUCCION
    /*
     * Columnas:
     * 0 = Nombre
     * 1 = Simbolo
     * 2 = Reaccion de oxidacion
     * 3 = Reaccion de reduccion
     * 4 = Potencial estandar de reduccion
     * 5 = Electrones
     */
    private static final String[][] elementos = {
            {"Oro", "Au", "Au -> Au3+ + 3e-", "Au3+ + 3e- -> Au", "1.50", "3"},
            {"Platino", "Pt", "Pt -> Pt2+ + 2e-", "Pt2+ + 2e- -> Pt", "1.20", "2"},
            {"Plata", "Ag", "Ag -> Ag+ + e-", "Ag+ + e- -> Ag", "0.80", "1"},
            {"Cobre", "Cu", "Cu -> Cu2+ + 2e-", "Cu2+ + 2e- -> Cu", "0.34", "2"},
            {"Plomo", "Pb", "Pb -> Pb2+ + 2e-", "Pb2+ + 2e- -> Pb", "-0.13", "2"},
            {"Estaño", "Sn", "Sn -> Sn2+ + 2e-", "Sn2+ + 2e- -> Sn", "-0.14", "2"},
            {"Niquel", "Ni", "Ni -> Ni2+ + 2e-", "Ni2+ + 2e- -> Ni", "-0.25", "2"},
            {"Cobalto", "Co", "Co -> Co2+ + 2e-", "Co2+ + 2e- -> Co", "-0.28", "2"},
            {"Cadmio", "Cd", "Cd -> Cd2+ + 2e-", "Cd2+ + 2e- -> Cd", "-0.40", "2"},
            {"Hierro", "Fe", "Fe -> Fe2+ + 2e-", "Fe2+ + 2e- -> Fe", "-0.44", "2"},
            {"Cromo", "Cr", "Cr -> Cr3+ + 3e-", "Cr3+ + 3e- -> Cr", "-0.74", "3"},
            {"Zinc", "Zn", "Zn -> Zn2+ + 2e-", "Zn2+ + 2e- -> Zn", "-0.76", "2"},
            {"Aluminio", "Al", "Al -> Al3+ + 3e-", "Al3+ + 3e- -> Al", "-1.66", "3"},
            {"Magnesio", "Mg", "Mg -> Mg2+ + 2e-", "Mg2+ + 2e- -> Mg", "-2.37", "2"},
            {"Sodio", "Na", "Na -> Na+ + e-", "Na+ + e- -> Na", "-2.71", "1"}
    };

    // METODO PRINCIPAL
    public static void main(String[] args) {

        int opcion = -1;
        boolean continuarPrograma = true;

        while (continuarPrograma) {

            System.out.println("===========================================");
            System.out.println("       CALCULADORA ELECTROQUIMICA");
            System.out.println("-------------------------------------------");
            System.out.println("1. Resolver celda galvanica");
            System.out.println("2. Resolver celda electrolitica");
            System.out.println("3. Mostrar tabla de materiales");
            System.out.println("4. Explicar anodo y catodo");
            System.out.println("0. Salir");

            opcion = leerEntero("Selecciona una opcion: ");

            if (opcion == 1) {
                resolverCelda(false);
            } else if (opcion == 2) {
                resolverCelda(true);
            } else if (opcion == 3) {
                mostrarTabla();
            } else if (opcion == 4) {
                explicarElectrodos();
            } else if (opcion == 0) {
                continuarPrograma = false;
                System.out.println("El programa ha finalizado.");
            } else {
                System.out.println("ERROR: La opcion no existe.");
                System.out.println("Ingresa una opcion del menu.");
            }
        }

        lector.close();
    }

    // METODO PARA LEER NUMEROS ENTEROS
    private static int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            try {
                String dato = lector.nextLine().trim();

                if (dato.isEmpty()) {
                    System.out.println("ERROR: No puedes dejar el espacio vacio.");
                } else {
                    return Integer.parseInt(dato);
                }

            } catch (NumberFormatException e) {
                System.out.println("ERROR: Debes ingresar un numero entero.");
            }
        }
    }

    // METODO PARA LEER NUMEROS DECIMALES
    private static double leerDecimal(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            try {
                String dato = lector.nextLine().trim();

                if (dato.isEmpty()) {
                    System.out.println("ERROR: No puedes dejar el espacio vacio.");
                } else {

                    double numero = Double.parseDouble(dato);

                    if (!Double.isFinite(numero)) {
                        System.out.println("ERROR: Ingresa un numero finito.");
                    } else if (numero <= 0) {
                        System.out.println("ERROR: La concentracion debe ser mayor que cero.");
                    } else {
                        return numero;
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("ERROR: Ingresa una cantidad decimal valida.");
                System.out.println("Ejemplo: 1.0, 0.5 o 0.25");
            }
        }
    }

    // METODO PARA MOSTRAR LA TABLA DE MATERIALES
    private static void mostrarTabla() {

        System.out.println("TABLA DE POTENCIALES ESTANDAR DE OXIDACIÓN");
        System.out.printf("%-15s %-8s %-15s%n",
                "MATERIAL", "SIMBOLO", "POTENCIAL");

        System.out.println("----------------------------------------------");

        for (int i = 0; i < elementos.length; i++) {

            System.out.printf("%-15s %-8s %.2f V%n",
                    elementos[i][0],
                    elementos[i][1],
                    Double.parseDouble(elementos[i][4]));
        }

        System.out.println("----------------------------------------------");
        System.out.println("Potenciales expresados en volts (V).");
    }

    // BUSCAR MATERIAL POR SIMBOLO
    private static int buscarElemento(String simbolo) {

        for (int i = 0; i < elementos.length; i++) {

            if (elementos[i][1].equals(simbolo)) {
                return i;
            }
        }

        return -1;
    }

    // METODO PARA RESOLVER CELDAS
    private static void resolverCelda(boolean electrolitica) {

        if (electrolitica) {
            System.out.println("CELDA ELECTROLITICA");
        } else {
            System.out.println("CELDA GALVANICA");
        }

        mostrarTabla();

        int indice1;
        int indice2;
        String simbolo1;
        String simbolo2;

        // VALIDAR PRIMER MATERIAL
        while (true) {

            System.out.print("Ingresa el simbolo del primer material: ");
            simbolo1 = lector.nextLine().trim();

            if (simbolo1.isEmpty()) {

                System.out.println("ERROR: El simbolo no puede estar vacio.");

            } else {

                indice1 = buscarElemento(simbolo1);

                if (indice1 == -1) {
                    System.out.println("ERROR: El material no esta registrado.");
                    System.out.println("Revisa la tabla y escribe el simbolo correctamente.");
                } else {
                    break;
                }
            }
        }

        // VALIDAR SEGUNDO MATERIAL
        while (true) {

            System.out.print("Ingresa el simbolo del segundo material: ");
            simbolo2 = lector.nextLine().trim();

            if (simbolo2.isEmpty()) {

                System.out.println("ERROR: El simbolo no puede estar vacio.");

            } else if (simbolo1.equals(simbolo2)) {

                System.out.println("ERROR: Debes seleccionar dos materiales diferentes.");

            } else {

                indice2 = buscarElemento(simbolo2);

                if (indice2 == -1) {
                    System.out.println("ERROR: El material no esta registrado.");
                } else {
                    break;
                }
            }
        }

        // SOLICITAR CONCENTRACIONES
        System.out.println("-------------------------------------------------------");
        System.out.println("CONCENTRACIONES");

        double concentracion1 = leerDecimal(
                "Concentracion de " + simbolo1 + " (mol/L): ");

        double concentracion2 = leerDecimal(
                "Concentracion de " + simbolo2 + " (mol/L): ");

        // OBTENER POTENCIALES ESTANDAR
        double potencial1 = Double.parseDouble(elementos[indice1][4]);
        double potencial2 = Double.parseDouble(elementos[indice2][4]);

        // IDENTIFICAR ANODO Y CATODO
        /*
         * El material con el potencial de reduccion mas negativo
         * se identifica como anodo.
         *
         * El material con el potencial de reduccion mas positivo
         * se identifica como catodo.
         */
        int indiceAnodo;
        int indiceCatodo;

        double potencialAnodo;
        double potencialCatodo;

        double concentracionAnodo;
        double concentracionCatodo;

        if (potencial1 < potencial2) {

            indiceAnodo = indice1;
            indiceCatodo = indice2;

            potencialAnodo = potencial1;
            potencialCatodo = potencial2;

            concentracionAnodo = concentracion1;
            concentracionCatodo = concentracion2;

        } else {

            indiceAnodo = indice2;
            indiceCatodo = indice1;

            potencialAnodo = potencial2;
            potencialCatodo = potencial1;

            concentracionAnodo = concentracion2;
            concentracionCatodo = concentracion1;
        }

        // OBTENER ELECTRONES DE CADA MATERIAL
        int electronesAnodo = Integer.parseInt(elementos[indiceAnodo][5]);
        int electronesCatodo = Integer.parseInt(elementos[indiceCatodo][5]);

        // CALCULAR EL MAXIMO COMUN DIVISOR
        int a = electronesAnodo;
        int b = electronesCatodo;

        while (b != 0) {

            int residuo = a % b;
            a = b;
            b = residuo;
        }

        int mcd = a;

        // CALCULAR MINIMO COMUN MULTIPLO
        int electronesTransferidos =
                (electronesAnodo * electronesCatodo) / mcd;

        // COEFICIENTES DE LAS SEMIRREACCIONES
        int coeficienteAnodo =
                electronesTransferidos / electronesAnodo;

        int coeficienteCatodo =
                electronesTransferidos / electronesCatodo;

        // FEM ESTANDAR
        /*
         * Se invierte el signo del potencial del catodo.
         *
         * FEM = potencial del catodo invertido + potencial del anodo.
         */
        double potencialCatodoInvertido = potencialCatodo * (-1);

        double femEstandar =
                potencialCatodoInvertido + potencialAnodo;

        // POTENCIAL ESTANDAR SEGUN EL TIPO DE CELDA
        double potencialEstandarCelda;

        if (electrolitica) {
            potencialEstandarCelda = femEstandar * (-1);
        } else {
            potencialEstandarCelda = femEstandar;
        }

        // ECUACION DE NERNST DEL ANODO
        /*
         * E anodo = E° anodo + (0.0592 / n anodo) log concentracion.
         *
         * Se utiliza la concentracion correspondiente al anodo.
         */
        double potencialNernstAnodo =
                potencialAnodo
                        + (CONSTANTE_NERNST / electronesAnodo)
                        * Math.log10(concentracionAnodo);

        // ECUACION DE NERNST DEL CATODO
        /*
         * E catodo = -E° catodo + (0.0592 / n catodo) log concentracion.
         *
         * Primero se invierte el signo del potencial estandar del catodo.
         * Despues se agrega el termino de Nernst.
         */
        double potencialCatodoInvertidoNernst =
                potencialCatodo * (-1);

        double potencialNernstCatodo =
                potencialCatodoInvertidoNernst
                        + (CONSTANTE_NERNST / electronesCatodo)
                        * Math.log10(concentracionCatodo);

        // SUMA DE LOS DOS POTENCIALES
        double potencialNernstGalvanico =
                potencialNernstAnodo + potencialNernstCatodo;

        // INVERTIR EL RESULTADO PARA LA CELDA ELECTROLITICA
        double potencialCelda;

        if (electrolitica) {
            potencialCelda = potencialNernstGalvanico * (-1);
        } else {
            potencialCelda = potencialNernstGalvanico;
        }

        // MOSTRAR RESULTADOS
        System.out.println("-------------------------------------------------------");
        System.out.println("RESULTADOS");

        System.out.println("Primer material: " + elementos[indice1][0]);
        System.out.println("Segundo material: " + elementos[indice2][0]);

        // MOSTRAR ANODO
        System.out.println("ANODO");

        if (electrolitica) {

            System.out.println("Material: " + elementos[indiceCatodo][0]);
            System.out.println("Simbolo: " + elementos[indiceCatodo][1]);
            System.out.println("Reaccion: " + elementos[indiceCatodo][2]);

        } else {

            System.out.println("Material: " + elementos[indiceAnodo][0]);
            System.out.println("Simbolo: " + elementos[indiceAnodo][1]);
            System.out.println("Reaccion: " + elementos[indiceAnodo][2]);
        }

        // MOSTRAR CATODO
        System.out.println("CATODO");

        if (electrolitica) {

            System.out.println("Material: " + elementos[indiceAnodo][0]);
            System.out.println("Simbolo: " + elementos[indiceAnodo][1]);
            System.out.println("Reaccion: " + elementos[indiceAnodo][3]);

        } else {

            System.out.println("Material: " + elementos[indiceCatodo][0]);
            System.out.println("Simbolo: " + elementos[indiceCatodo][1]);
            System.out.println("Reaccion: " + elementos[indiceCatodo][3]);
        }

        // OXIDACION Y REDUCCION
        System.out.println("OXIDACION Y REDUCCION");

        if (electrolitica) {

            System.out.println("Oxidacion: " + elementos[indiceCatodo][2]);
            System.out.println("Reduccion: " + elementos[indiceAnodo][3]);

        } else {

            System.out.println("Oxidacion: " + elementos[indiceAnodo][2]);
            System.out.println("Reduccion: " + elementos[indiceCatodo][3]);
        }

        // BALANCE DE ELECTRONES
        System.out.println("-------------------------------------------------------");
        System.out.println("BALANCE DE ELECTRONES");

        System.out.println("Electrones del anodo: " + electronesAnodo);
        System.out.println("Electrones del catodo: " + electronesCatodo);
        System.out.println("Minimo comun multiplo: " + electronesTransferidos);
        System.out.println("Coeficiente del anodo: " + coeficienteAnodo);
        System.out.println("Coeficiente del catodo: " + coeficienteCatodo);
        System.out.println("Electrones transferidos: " + electronesTransferidos);

        // POTENCIAL DE CELDA
        System.out.println("\n-------------------------------------------------------");
        System.out.println("POTENCIAL DE CELDA");

        System.out.printf("Potencial estandar de oxidación del anodo: %.2f V%n",
                potencialAnodo);

        System.out.printf("Potencial estandar de oxidación del catodo: %.2f V%n",
                potencialCatodo);

        System.out.printf("Potencial del catodo con signo invertido: %.2f V%n",
                potencialCatodoInvertido);

        System.out.printf("FEM estandar de la reaccion galvanica: %.2f V%n",
                femEstandar);

        if (electrolitica) {
            System.out.printf("Potencial estandar de la reaccion inversa: %.2f V%n",
                    potencialEstandarCelda);
        }

        // ECUACIONES DE NERNST
        System.out.println("-------------------------------------------------------");
        System.out.println("ECUACIONES DE NERNST");

        System.out.printf("Temperatura: %.2f grados Celsius%n", TEMPERATURA);

        System.out.printf("Concentracion del anodo (%s): %.2f mol/L%n",
                elementos[indiceAnodo][1], concentracionAnodo);

        System.out.printf("Concentracion del catodo (%s): %.2f mol/L%n",
                elementos[indiceCatodo][1], concentracionCatodo);

        System.out.println("Ecuacion del anodo:");
        System.out.println("E anodo = E° anodo + (0.0592 / n anodo) log concentracion");

        System.out.printf("Potencial Nernst del anodo: %.2f V%n",
                potencialNernstAnodo);

        System.out.println("Ecuacion del catodo:");
        System.out.println("E catodo = -E° catodo + (0.0592 / n catodo) log concentracion");

        System.out.printf("Potencial Nernst del catodo: %.2f V%n",
                potencialNernstCatodo);

        System.out.println("Suma de los potenciales:");
        System.out.println("E celda = E anodo + E catodo");

        System.out.printf("Potencial calculado con Nernst: %.2f V%n",
                potencialCelda);

        // CONCLUSION
        System.out.println("-------------------------------------------------------");
        System.out.println("CONCLUSION");

        if (electrolitica) {

            System.out.println("Tipo de celda: ELECTROLITICA");
            System.out.println("La reaccion inversa necesita energia electrica externa.");

            System.out.printf("Magnitud del potencial calculado: %.2f V%n",
                    Math.abs(potencialCelda));

        } else {

            System.out.println("Tipo de celda: GALVANICA");

            if (potencialCelda > 0) {

                System.out.println("El potencial calculado es positivo.");

            } else if (potencialCelda < 0) {

                System.out.println("El potencial calculado es negativo.");

            } else {

                System.out.println("La celda se encuentra en equilibrio.");
            }
        }

        System.out.println("-------------------------------------------------------");
    }

    // EXPLICACION DE ANODO Y CATODO
    private static void explicarElectrodos() {

        System.out.println("EXPLICACION DE ELECTRODOS");

        System.out.println("ANODO");
        System.out.println("Es el electrodo donde ocurre la oxidacion.");
        System.out.println("En este electrodo la especie pierde electrones.");

        System.out.println("CATODO");
        System.out.println("Es el electrodo donde ocurre la reduccion.");
        System.out.println("En este electrodo la especie gana electrones.");

        System.out.println("OXIDACION");
        System.out.println("Es el proceso en el que una especie pierde electrones.");
        System.out.println("Ejemplo: Zn -> Zn2+ + 2e-");

        System.out.println("REDUCCION");
        System.out.println("Es el proceso en el que una especie gana electrones.");
        System.out.println("Ejemplo: Cu2+ + 2e- -> Cu");

        System.out.println("CELDA GALVANICA");
        System.out.println("Produce energia electrica mediante una reaccion espontanea.");

        System.out.println("CELDA ELECTROLITICA");
        System.out.println("Necesita una fuente externa de energia electrica.");

        System.out.println("POTENCIAL DE CELDA");
        System.out.println("Se calcula sumando los potenciales del anodo y del catodo.");
        System.out.println("E celda = E anodo + E catodo");

        System.out.println("ECUACIONES DE NERNST");

        System.out.println("E anodo = E° anodo + (0.0592 / n anodo) log concentracion");

        System.out.println("E catodo = -E° catodo + (0.0592 / n catodo) log concentracion");

        System.out.println("E celda = E anodo + E catodo");
    }
}