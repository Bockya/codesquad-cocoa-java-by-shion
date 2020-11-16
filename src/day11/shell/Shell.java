package day11.shell;

import java.util.Scanner;

public class Shell {

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();
        shell.startMainShell();
    }

    public void startMainShell() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Java Shell> ");
            String command = sc.nextLine();
            History.saveInHistory(command);
            if (command.equalsIgnoreCase("ls")) { // Runnable 구현
                Runnable list = new List();
                Thread listThread = new Thread(list);
                listThread.start();
                Thread.sleep(10);
            }
            if (command.equalsIgnoreCase("pwd")) { // Runnalbe 구현
                Thread directoryThread = new Thread(new Directory());
                directoryThread.start();
                Thread.sleep(10);
            }
            if (command.equalsIgnoreCase("history")) { // Thread 상속
                History historyThread = new History();
                historyThread.start();
                Thread.sleep(10);
            }
            if (command.startsWith("cp ")) { // Runnable 구현
                String[] files = command.split(" ");
                String targetFile = files[1];
                String copiedFile = files[2];
                Runnable copy = new Copy(targetFile, copiedFile);
                Thread copyThread = new Thread(copy);
                copyThread.start();
                Thread.sleep(10);
            }
        }
    }
}
