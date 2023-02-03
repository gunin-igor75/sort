package ru.gil.sort;

import ru.gil.exception.NotElementHeapException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.logging.Logger;

public class MergeSortInt {
    private static Logger LOG = Logger.getLogger(MergeSortInt.class.getName());
    private HeapSortInt heap;

    public MergeSortInt(HeapSortInt heap) {
        this.heap = heap;
    }


    public void mainMethod(Path outFile, ArrayDeque<Path> inFiles) {
        int number = 1;
        String nameFile = "out" + number + ".txt";
        if (inFiles.size() == 1) {
            Path inFile = mergeInOneOut(inFiles.poll(), nameFile);
            if (inFile != null) {
                inFiles.addFirst(inFile);
            }
        }
        while (inFiles.size() > 1) {
            Path fileOne = inFiles.poll();
            Path fileTwo = inFiles.poll();
            try (BufferedReader inOne = Files.newBufferedReader(fileOne);
                 BufferedReader inTwo = Files.newBufferedReader(fileTwo);
                 PrintWriter writer = new PrintWriter(nameFile)) {
                mergeSort(inOne, inTwo, writer);
                // Добавление в начало очереди
                inFiles.addFirst(Paths.get(nameFile));
                number++;
                Files.delete(fileOne);
                Files.delete(fileTwo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // если куча(не валидные элементы) не пустая
        if (heap.getSize() != 0) {
            mergeHeapOut(outFile, inFiles.poll());
        }
    }

    // Сортировка 2 файлов
    private void mergeSort(BufferedReader inOne, BufferedReader inTwo, PrintWriter writer) throws IOException {
        int numOne = nextInt(inOne);
        int numTwo = nextInt(inTwo);
        // Изначально в 2 файлах числа валидные
        int minCurrent = Math.min(numOne, numTwo);
        while (inOne.ready() && inTwo.ready()) {
            if (numOne <= numTwo && numOne >= minCurrent) {
                minCurrent = numOne;
                writer.println(numOne);
                numOne = nextInt(inOne);
            } else if (numTwo < numOne && numTwo >= minCurrent) {
                minCurrent = numTwo;
                writer.println(numTwo);
                numTwo = nextInt(inTwo);
            } else if (numOne < minCurrent) {
                heap.insert(numOne);
                numOne = nextInt(inOne);
            } else {
                heap.insert(numTwo);
                numTwo = nextInt(inTwo);
            }
        }
        if (!inOne.ready()) {
            mergeOneTwo(inTwo, minCurrent, numOne, writer);
        } else {
            mergeOneTwo(inOne, minCurrent, numTwo, writer);
        }
    }

    // Сортировка результирующего файла и остатков из кучи
    private void mergeHeapOut(Path outFile, Path inFile) {
        int numOne;
        int numTwo;
        try (BufferedReader in = new BufferedReader(new FileReader(inFile.toFile()));
             PrintWriter writer = new PrintWriter(outFile.toString())) {
            numOne = nextInt(in);
            numTwo = heap.extractMin();
            while (in.ready()) {
                if (numOne < numTwo) {
                    writer.println(numOne);
                    numOne = nextInt(in);
                } else {
                    writer.println(numTwo);
                    try {
                        numTwo = heap.extractMin();
                    } catch (NotElementHeapException ex) {
                        break;
                    }
                }
            }
            if (heap.getSize() == 0) {
                while (in.ready()) {
                    numOne = nextInt(in);
                    writer.println(numOne);
                }
            } else {
                while (heap.getSize() > 0) {
                    numTwo = heap.extractMin();
                    writer.println(numTwo);
                }
            }

        } catch (IOException e) {
            String message = "heap merge error";
            LOG.warning(message);
            e.printStackTrace(System.out);
        }
    }

    // Считывание и парсинг int
    private Integer nextInt(BufferedReader reader) throws IOException {
        String numberString;
        Integer number = null;
        while ((numberString = reader.readLine()) != null) {
            try {
                number = Integer.parseInt(numberString);
                break;
            } catch (NumberFormatException ex) {
                LOG.warning("Not a valid number");
            }
        }
        return number;
    }


    // Слияние
    private void mergeOneTwo(BufferedReader reader, int minCurrent, int endNum,
                             PrintWriter writer) throws IOException {
        int number = nextInt(reader);
        boolean flag = false;
        while (reader.ready()) {
            if (number < endNum && number >= minCurrent) {
                writer.println(number);
                minCurrent = number;
                number = nextInt(reader);
            } else if (endNum <= number && endNum >= minCurrent) {
                writer.println(endNum);
                flag = true;
            } else if (endNum < minCurrent) {
                heap.insert(endNum);
            } else {
                heap.insert(number);
            }
        }
        if (!flag) {
            writer.println(endNum);
        }
    }

    // Слияние если у нас пришел один входящий файл
    private Path mergeInOneOut(Path infile, String outFile){
        try (BufferedReader in = Files.newBufferedReader(infile);
             PrintWriter writer = new PrintWriter(outFile)) {
            int number = nextInt(in);
            int minCurrent = number;
            while (in.ready()) {
                if (number >= minCurrent) {
                    writer.println(number);
                    minCurrent = number;
                } else {
                    heap.insert(number);
                }
                number = nextInt(in);
            }
            return Paths.get(outFile);
        } catch (IOException ex) {
            LOG.severe("File opening error");
            ex.printStackTrace(System.out);
        }
        return null;
    }
}