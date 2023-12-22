package ru.mirea.goncharov.task1;

import java.io.*;
import java.util.Scanner;

public class ProcessorTree {
    private TreeNode root;

    public ProcessorTree() {
        this.root = null;
    }

    public void insert(Processor processor) {
        root = insertRec(root, processor);
    }

    private TreeNode insertRec(TreeNode root, Processor processor) {
        if (root == null) {
            return new TreeNode(processor);
        }

        if (processor.key < root.processor.key) {
            root.left = insertRec(root.left, processor);
        } else if (processor.key == root.processor.key) {
            System.out.println("Record with key " + processor.key + " already exists.");
        } else if (processor.key > root.processor.key && root.middle == null) {
            root.middle = new TreeNode(processor);
        } else if (processor.key > root.processor.key) {
            root.right = insertRec(root.right, processor);
        }

        return root;
    }

    public void delete(int key) {
        root = deleteRec(root, key);
    }

    private TreeNode deleteRec(TreeNode root, int key) {
        if (root == null) {
            System.out.println("Record with key " + key + " not found.");
            return null;
        }

        if (key < root.processor.key) {
            root.left = deleteRec(root.left, key);
        } else if (key > root.processor.key) {
            root.right = deleteRec(root.right, key);
        } else {
            if (root.middle != null) {
                System.out.println("Record with key " + key + " has multiple values. Specify a different key for deletion.");
            } else {
                if (root.right != null) {
                    TreeNode min = findMin(root.right);
                    root.processor = min.processor;
                    root.right = deleteRec(root.right, min.processor.key);
                } else if (root.left != null) {
                    TreeNode max = findMax(root.left);
                    root.processor = max.processor;
                    root.left = deleteRec(root.left, max.processor.key);
                } else {
                    root = null;
                }
            }
        }

        return root;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private TreeNode findMax(TreeNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public void printLevels() {
        int height = height(root);
        for (int i = height; i >= 1; i--) {
            printLevel(root, i);
        }
    }

    private void printLevel(TreeNode root, int level) {
        if (root == null) {
            return;
        }
        if (level == 1) {
            if (root.middle != null) {
                System.out.print(root.processor.key + " - ");
            } else {
                System.out.print(root.processor.key + " ");
            }
        } else if (level > 1) {
            printLevel(root.left, level - 1);
            printLevel(root.middle, level - 1);
            printLevel(root.right, level - 1);
        }
    }

    private int height(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int leftHeight = height(root.left);
            int middleHeight = height(root.middle);
            int rightHeight = height(root.right);

            return Math.max(leftHeight, Math.max(middleHeight, rightHeight)) + 1;
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            saveToFile(root, writer);
            System.out.println("Data saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(TreeNode root, PrintWriter writer) {
        if (root == null) {
            return;
        }

        saveToFile(root.left, writer);

        writer.println(root.processor.key + ", " + root.processor.name + ", " + root.processor.clockFrequency
                + ", " + root.processor.cacheSize + ", " + root.processor.busFrequency
                + ", " + root.processor.specInt + ", " + root.processor.specFp);

        saveToFile(root.middle, writer);

        saveToFile(root.right, writer);
    }

    public static void main(String[] args) {
        ProcessorTree processorTree = new ProcessorTree();

        // Reading data from the file and building the tree
        try (Scanner scanner = new Scanner(new File("C:\\Users\\Artem\\IdeaProjects\\JavaLab31\\src\\ru\\mirea\\goncharov\\task1\\PROCS.txt"))) {
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(", ");
                int key = Integer.parseInt(tokens[0]);
                String name = tokens[1];
                double clockFrequency = Double.parseDouble(tokens[2]);
                int cacheSize = Integer.parseInt(tokens[3]);
                double busFrequency = Double.parseDouble(tokens[4]);
                int specInt = Integer.parseInt(tokens[5]);
                int specFp = Integer.parseInt(tokens[6]);

                Processor processor = new Processor(key, name, clockFrequency, cacheSize, busFrequency, specInt, specFp);
                processorTree.insert(processor);
            }
            System.out.println("Data loaded from file successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner userInput = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nEnter a command (L, D n, A n, S, E): ");
            String command = userInput.next();

            switch (command) {
                case "L":
                    System.out.println("\nProcessor Tree Nodes:");
                    processorTree.printLevels();
                    break;
                case "D":
                    int deleteKey = userInput.nextInt();
                    processorTree.delete(deleteKey);
                    break;
                case "A":
                    int addKey = userInput.nextInt();
                    // Read processor details from user
                    System.out.print("Enter processor name: ");
                    String addName = userInput.next();
                    System.out.print("Enter clock frequency: ");
                    double addClockFrequency = userInput.nextDouble();
                    System.out.print("Enter cache size: ");
                    int addCacheSize = userInput.nextInt();
                    System.out.print("Enter bus frequency: ");
                    double addBusFrequency = userInput.nextDouble();
                    System.out.print("Enter SPECint result: ");
                    int addSpecInt = userInput.nextInt();
                    System.out.print("Enter SPECfp result: ");
                    int addSpecFp = userInput.nextInt();

                    Processor addedProcessor = new Processor(addKey, addName, addClockFrequency,
                            addCacheSize, addBusFrequency, addSpecInt, addSpecFp);

                    processorTree.insert(addedProcessor);
                    break;
                case "S":
                    processorTree.saveToFile("PROCS.TXT");
                    break;
                case "E":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid command. Please enter a valid command.");
            }
        }

        System.out.println("Exiting the program.");
    }
}
