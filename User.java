package com.gmail;

import java.util.*;

public class User {

    private final String firstName, lastName, dob, contact, gmail, password;

    private final List<Mail> inbox = new ArrayList<>();
    private final List<Mail> sent = new ArrayList<>();
    private final List<Mail> drafts = new ArrayList<>();
    private final List<Mail> bin = new ArrayList<>();

    private int unreadCount = 0;

    public User(String firstName, String lastName, String dob, String contact, String gmail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.contact = contact;
        this.gmail = gmail;
        this.password = password;
    }

    public String getMail() { return gmail; }
    public String getPassword() { return password; }
    public int getUnreadCount() { return unreadCount; }

    public void receiveMail(Mail m) {
        inbox.add(m);
        unreadCount++;
    }

    public void addSent(Mail m) { sent.add(m); }
    public void addDraft(Mail m) { drafts.add(m); }

    public void clearUnread() { unreadCount = 0; }

    public boolean deleteMail(int id) {
        for (Iterator<Mail> it = sent.iterator(); it.hasNext(); ) {
            Mail m = it.next();
            if (m.getId() == id) {
                it.remove();
                bin.add(m);
                return true;
            }
        }

        for (Iterator<Mail> it = inbox.iterator(); it.hasNext(); ) {
            Mail m = it.next();
            if (m.getId() == id) {
                it.remove();
                bin.add(m);
                return true;
            }
        }

        return false;
    }

    public void printInbox() { inbox.forEach(Mail::display); }
    public void printSent() { sent.forEach(Mail::display); }
    public void printDrafts() { drafts.forEach(Mail::display); }
    public void printBin() { bin.forEach(Mail::display); }
}
