//    미션 3 가계부 구현하기
//    필수 요구사항
//    간단한 가계부를 구현한다.
//    키보드를 통해 데이터 입력을 받고 화면에 내용을 출력한다.
//    사용자 등록: 사용자 이름 및 비밀번호를 입력받는다. --- ok
//    데이터 입력: 날짜, 적요, 수입, 지출을 입력받는다. --- ok
//    데이터 삭제: 특정 순번의 데이터를 삭제한다.
//    데이터 수정: 특정 순번의 데이터를 수정할 수 있다.
//    화면에 출력: 해당 월의 지출내역을 순번, 적요, 수입, 지출, 잔액으로 화면에 출력한다.

//    미션4 가계부에 검색 기능 추가하기
//    가계부 추가 요구사항
//    자동 저장 기능을 추가한다. 프로그램 종료 후 다시 시작해도 데이터가 보존되도록 구현해 보자. --- ok
//    소비 유형을 추가한다. (현금 / 카드)
//    검색 기능을 구현한다. 적요, 날짜, 금액, 수입, 지출, 소비 유형별로 검색을 하고 결과를 표시할 수 있어야 한다.
//    정렬해서 보여주기 기능을 추가한다. 날짜 또는 금액의 오름차순 또는 내림차순으로 정렬해서 화면에 출력할 수 있어야 한다.

package day08;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class AccountingBook {
    public static Scanner sc = new Scanner(System.in);
    private static Hashtable<String, String> userListMap = new Hashtable<>();
    private final String userListPath = "/Users/shion/Desktop/";
    private final String userDataDirPath = "/Users/shion/Desktop/user_data/";
    private final String userListFileName = "user_list.txt";

    public static void main(String[] args) {
        AccountingBook accountingBook = new AccountingBook();
        accountingBook.init();
    }

    public void init() {
        createUserListFile();
        readUserListFromFile(); // file에서 읽어서 맵에 추가한다.
        readUserListFormMap();
        showMain();
    }

    public void showMain() {

        System.out.println("┌-------------┐");
        System.out.println("|    가계부     |");
        System.out.println("┌---------------------------┐");
        System.out.println("| (1) 사용자 입력 (2) 사용자 등록 |");
        System.out.println("└---------------------------┘");
        System.out.print(" >> ");
        choiceSignInOrSignUp();
    }

    public void choiceSignInOrSignUp() {
        int input = sc.nextInt();
        sc.nextLine(); // 버퍼비우기
        if (input == Membership.SIGN_IN.getIndex()) {
            signIn();
        }
        if (input == Membership.SIGN_UP.getIndex()) {
            signUp();
        }
    }

    // 중복확인 기능 추가하기
    public void signUp() {
        System.out.println("┌---------------------------┐");
        System.out.println("| 사용자와 비밀번호를 입력해주세요. |");
        System.out.println("└---------------------------┘");
        System.out.print(" 사용자 >> ");
        String userName = sc.nextLine();
        System.out.print(" 비밀번호 >> ");
        String userPassword = sc.nextLine();
        System.out.println("[ 사용자(" + userName + ") 등록되었습니다. ]");

        addUserToFile(userName + " " + userPassword);
        addUserToMap(userName, userPassword);
        showMain();
    }

    public void addUserToFile(String userNameAndPassword) {
        File file = createUserListFile();
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(userNameAndPassword + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        createUserDataFile(userNameAndPassword.split(" ")[0]);
    }

    public File createUserListFile() {
        File file = null;
        try {
            file = new File(userListPath + userListFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void createUserDataDir() {
        File userDataDir = null;
        try {
            userDataDir = new File(userDataDirPath);
            if (!userDataDir.exists() || !userDataDir.isDirectory()) {
                userDataDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUserDataFile(String userName) {
        createUserDataDir();
        try {
            File userData = new File(userDataDirPath + userName);
            if (!userData.exists() || !userData.isFile()) {
                userData.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readUserListFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(userListPath + userListFileName))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(" ");
                String userName = userInfo[0];
                String userPassword = userInfo[1];
                addUserToMap(userName, userPassword);
            }

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // for test
    public void readUserListFormMap() {
        Enumeration e = userListMap.keys();
        while (e.hasMoreElements()) {
            Object keyObj = e.nextElement();
            String key = (String) keyObj;
            Object valObj = userListMap.get(key);
            String val = (String) valObj;
            System.out.println("key: " + key + ", val: " + val);
        }
    }

    public void addUserToMap(String userName, String userPassword) {
        userListMap.put(userName, userPassword);
    }

    public void signIn() {
        System.out.println("┌---------------------------┐");
        System.out.println("| 사용자와 비밀번호를 입력해주세요. |");
        System.out.println("└---------------------------┘");
        System.out.print(" 사용자 >> ");
        String userName = sc.nextLine();
        if (isUser(userName)) {
            inputUserPassword(userName);
        }
        if (!isUser(userName)) {
            System.out.println("[ ! 사용자(" + userName + ")가 존재하지 않습니다. ]");
            System.out.println("[ (1) 다시입력 (2) 메인으로 돌아가기 ]");
            String input = sc.nextLine();
            if (input.equals("1")) signIn();
            if (input.equals("2")) showMain();

        }
    }

    public boolean isUser(String userName) {
        if (userListMap.containsKey(userName)) {
            return true;
        }
        return false;
    }

    public void inputUserPassword(String userName) {
        System.out.print(" 비밀번호 >> ");
        String userPassword = sc.nextLine();
        if (isCorrectPassword(userName, userPassword)) {
            showPersonalMenu(userName);
        }
        if (!isCorrectPassword(userName, userPassword)) {
            System.out.println("[ ! 비밀번호가 틀렸습니다. ]");
            System.out.println("[ (1) 다시입력 (2) 메인으로 돌아가기 ]");
            String input = sc.nextLine();
            if (input.equals("1")) inputUserPassword(userName);
            if (input.equals("2")) showMain();
        }
    }

    public boolean isCorrectPassword(String userName, String userPassword) {
        if (userPassword.equals(userListMap.get(userName))) {
            return true;
        }
        return false;
    }

    public void showPersonalMenu(String userName) {
        File userPersonalDataFile = null;
        try {
            userPersonalDataFile = new File(userDataDirPath + userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("[ " + userName + "의 가계부 ]");
            System.out.println("┌------------------------------------------------------------┐");
            System.out.println("| <(1)입력> <(2)삭제> <(3)수정> <(4)출력> <(5)검색> <(0)메인화면으로> |");
            System.out.println("└------------------------------------------------------------┘");
            System.out.print(" >> ");
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    inputDataToPersonalDataFile(userPersonalDataFile);
                    break;
                case "2":
                    deleteUserPersonalData(userPersonalDataFile);
                    break;
                case "3":
                    break;
                case "4":
                    showUserPersonalData(userPersonalDataFile);
                    break;
                case "5":
                    break;
                case "0":
                    showMain();
                    break;
                default:
                    break;
            }
        }
    }

    public void inputDataToPersonalDataFile(File userPersonalDataFile) {
        StringBuilder data = new StringBuilder("");
        System.out.print("날짜(2020-11-11) : ");
        String date = sc.nextLine();
        System.out.print("적요(comment) : ");
        String comment = sc.nextLine();
        System.out.print("수입(income) : ");
        String income = sc.nextLine();
        System.out.print("지출(expense) : ");
        String expense = sc.nextLine();
        data.append("날짜 : " + date).
                append(", 적요 : " + comment).
                append(", 수입 : " + income).
                append(", 지출 : " + expense);
        System.out.print(data.toString());

        try (FileWriter fw = new FileWriter(userPersonalDataFile, true)) {
            fw.write(data.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserPersonalData(File userPersonalDataFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(userPersonalDataFile))) {
            String line = "";
            int index = 0;
            while ((line = br.readLine()) != null) {
                index++;
                System.out.println("[" + index + "] " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserPersonalData(File userPersonalDataFile) {
        System.out.println("[ 몇번째 데이터를 삭제하시겠습니까? ]");
        System.out.print(" >> ");
        String input = sc.nextLine();
        String targetLine = "";
        FileWriter fw = null;
        try (BufferedReader br = new BufferedReader(new FileReader(userPersonalDataFile));) {

//            fw = new FileWriter(userPersonalDataFile);

            String line = "";
            int index = 0;

            while ((line = br.readLine()) != null) {
                index++;
                System.out.println("[" + index + "] " + line);
                if (input.equals(index + "")) {
                    System.out.println("[" + index + "번째 데이터가 삭제되었습니다.]  " + line);
                    targetLine = line;
                    continue;
                }
//                fw.append(line + "\n");
//                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
