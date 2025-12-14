package com.gmail;

public class Mail {

    private static int generator = 1;

    private final int id;
    private final String sender;
    private String receiver;
    private String subject;
    private String body;

    public Mail(String sender, String receiver, String subject, String body) {
        this.id = generator++;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    public int getId() { return id; }
    public String getReceiver() { return receiver; }

    public void display() {
        System.out.println("Mail ID: " + id);
        System.out.println("From: " + sender);
        System.out.println("To: " + receiver);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("------------------------------");
    }
}
