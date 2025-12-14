package com.gmail;

import java.util.*;

public class GmailApp {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, User> userMap = new HashMap<>();
    private User currentUser;

    public void launch() {
        while (true) {
            System.out.println("\n===== Welcome to Gmail Console Application =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter option: ");

            int op = readInt();

            switch (op) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Exiting Gmail... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createAccount() {
        System.out.println("\n===== CREATE ACCOUNT =====");

        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("DOB: ");
        String dob = scanner.nextLine();

        System.out.print("Contact: ");
        String contact = scanner.nextLine();

        String gmail;
        while (true) {
            System.out.print("Enter Gmail ID: ");
            gmail = scanner.nextLine();

            if (!gmail.endsWith("@gmail.com"))
                gmail += "@gmail.com";

            if (userMap.containsKey(gmail)) {
                System.out.println("\nThis Gmail ID already exists!");
                System.out.println("1. Choose from suggestions");
                System.out.println("2. Enter manually");
                System.out.print("Enter choice: ");

                int ch = readInt();
                if (ch == 1) {
                    String[] suggestions = generateSuggestions(firstName);

                    System.out.println("\nSuggestions:");
                    for (int i = 0; i < 3; i++) {
                        System.out.println((i + 1) + ". " + suggestions[i]);
                    }
                    System.out.print("Select (1-3): ");

                    int opt = readInt();
                    if (opt >= 1 && opt <= 3) {
                        gmail = suggestions[opt - 1];
                        break;
                    }
                }
            } else {
                break;
            }
        }

        String password;
        while (true) {
            System.out.print("Password (min 8 chars): ");
            password = scanner.nextLine();
            if (password.length() >= 8) break;
            System.out.println("Password too short!");
        }

        User newUser = new User(firstName, lastName, dob, contact, gmail, password);
        userMap.put(gmail, newUser);

        System.out.println("\nAccount created successfully!");
    }

    private void login() {
        System.out.println("\n===== LOGIN =====");

        while (true) {
            System.out.print("Enter Gmail ID: ");
            String gmail = scanner.nextLine();

            if (!gmail.endsWith("@gmail.com"))
                gmail += "@gmail.com";

            User user = userMap.get(gmail);

            if (user == null) {
                System.out.println("User not found!");
                if (!askYesNo("Try again?")) return;
                continue;
            }

            while (true) {
                System.out.print("Enter password: ");
                String pwd = scanner.nextLine();

                if (pwd.equals(user.getPassword())) {
                    currentUser = user;
                    System.out.println("\nLogin successful!\n");
                    homePage();
                    return;
                } else {
                    System.out.println("Incorrect password!");
                    if (!askYesNo("Try again?")) return;
                }
            }
        }
    }

  
    private void homePage() {
        while (true) {
            System.out.println("===== HOME PAGE =====");
            System.out.println("1. Compose Mail");
            System.out.println("2. All Mails");
            System.out.println("3. Inbox [" + currentUser.getUnreadCount() + "]");
            System.out.println("4. Sent");
            System.out.println("5. Draft");
            System.out.println("6. Bin");
            System.out.println("7. Logout");
            System.out.print("Enter option: ");

            int op = readInt();

            switch (op) {
                case 1 -> composeMail();
                case 2 -> showAllMails();
                case 3 -> showInbox();
                case 4 -> showSent();
                case 5 -> showDrafts();
                case 6 -> showBin();
                case 7 -> {
                    System.out.println("Logged out successfully.");
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }
  
    private void composeMail() {
        System.out.println("\n===== COMPOSE MAIL =====");

        String receiver = getValidReceiver();

        System.out.print("Subject: ");
        String subject = scanner.nextLine();

        System.out.print("Body: ");
        String body = scanner.nextLine();

        Mail mail = new Mail(currentUser.getMail(), receiver, subject, body);

        if (askYesNo("Send mail?")) {
            sendMail(mail);
        } else {
            currentUser.addDraft(mail);
            System.out.println("Mail saved to drafts.");
        }
    }

    private void sendMail(Mail mail) {
        currentUser.addSent(mail);

        User receiverUser = userMap.get(mail.getReceiver());
        if (receiverUser != null) {
            receiverUser.receiveMail(mail);
        }

        System.out.println("Mail sent successfully!");
    }

    private void showInbox() {
        System.out.println("\n===== INBOX =====");
        currentUser.printInbox();
        currentUser.clearUnread();
    }

    private void showSent() {
        System.out.println("\n===== SENT MAILS =====");
        currentUser.printSent();
    }

    private void showDrafts() {
        System.out.println("\n===== DRAFTS =====");
        currentUser.printDrafts();
    }

    private void showBin() {
        System.out.println("\n===== BIN =====");
        currentUser.printBin();
    }

    private void showAllMails() {
        System.out.println("\n===== ALL MAILS =====");
        currentUser.printSent();
        System.out.println("-------------------");
        currentUser.printInbox();

        if (askYesNo("Delete a mail?")) {
            System.out.print("Enter mail ID: ");
            int id = readInt();

            if (currentUser.deleteMail(id)) {
                System.out.println("Mail moved to bin.");
            } else {
                System.out.println("Invalid mail number!");
            }
        }
    }
    private boolean askYesNo(String msg) {
        System.out.print(msg + " (yes/no): ");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }

    private int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (Exception e) {
                System.out.print("Enter valid number: ");
            }
        }
    }

    private String getValidReceiver() {
        while (true) {
            System.out.print("To: ");
            String rec = scanner.nextLine();

            if (!rec.endsWith("@gmail.com"))
                rec += "@gmail.com";

            if (userMap.containsKey(rec))
                return rec;

            System.out.println("Gmail does not exist!");
        }
    }

    private String[] generateSuggestions(String name) {
        String[] s = new String[3];
        int index = 0;
        Random r = new Random();

        while (index < 3) {
            String gmail = name + (r.nextInt(900) + 100) + "@gmail.com";

            if (!userMap.containsKey(gmail)) {
                s[index++] = gmail;
            }
        }
        return s;
    }

    public static void main(String[] args) {
        new GmailApp().launch();
    }
}
