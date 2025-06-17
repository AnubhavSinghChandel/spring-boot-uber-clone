package com.project.taxiGo.taxiGoApp.services;

public interface EmailSendingService {

    public void sendEmail(String to, String subject, String body);

    public void sendBulkEmail(String[] to, String subject, String body);
}
