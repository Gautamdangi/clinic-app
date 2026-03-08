package com.clinicapp.appointment.service;

import com.clinicapp.appointment.model.Appointment;
import com.clinicapp.appointment.model.Status;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
   @Autowired
   private JavaMailSender javaMailSender;

//method 1

//    //simple mail without attachment
//    public String sendEmail(EmailDetails details) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(details.getRecipient());
//        mailMessage.setSubject(details.getSubject());
//        mailMessage.setText(details.getMsgBody());
//
//        javaMailSender.send(mailMessage);
//
//
//        return "Mail Sent Successfully";
//    }


    public void sendPatientEmail(Appointment appointment) {

        String subject = STR."Appointment\{appointment.getStatus()}";
        String msgBody = buildMsgBody(appointment);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(appointment.getPatient().getEmail());
            helper.setSubject(subject);
            helper.setText(msgBody, true);

            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e){
            e.printStackTrace();
        }


    }
    private String buildMsgBody(Appointment appointment) {

        String statusMessage;

        if (appointment.getStatus() == Status.SCHEDULED) {
            statusMessage = "Your appointment has been successfully scheduled";
        } else if (appointment.getStatus() == Status.RESCHEDULED) {
            statusMessage = "Your appointment has been rescheduled";
        } else if (appointment.getStatus() == Status.CANCELED) {
            statusMessage = "Your appointment has been cancelled";
        } else {
            statusMessage = "Your appointment has been completed";
        }

        return """
            <html>
            <body>
                <h2>Appointment Update</h2>
                <p>Dear <b>%s</b>,</p>

                <p>%s</p>

                <h3>Appointment Details</h3>
                <ul>
                    <li><b>Doctor:</b> %s</li>
                    <li><b>Date & Time:</b> %s</li>
                    <li><b>Status:</b> %s</li>
                </ul>

                <p>Thank you for choosing our clinic.</p>
                <p><b>Clinic Appointment System</b></p>
            </body>
            </html>
        """.formatted(
                appointment.getPatient().getName(),
                statusMessage,
                appointment.getDoctor().getName(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
    @Async
    public void sendAppointmentEmail(Appointment appointment) {
        sendPatientEmail(appointment);
        sendDoctorEmail(appointment);
    }

    private void sendDoctorEmail(Appointment appointment) {

        String msg = """
        <html>
        <body>
            <h2>New Appointment Booked</h2>

            <p>Dear Dr. %s,</p>

            <p>You have a new appointment.</p>

            <ul>
                <li><b>Patient:</b> %s</li>
                <li><b>Date & Time:</b> %s</li>
            </ul>

        </body>
        </html>
    """.formatted(
                appointment.getDoctor().getName(),
                appointment.getPatient().getName(),
                appointment.getAppointmentTime()
        );

        sendEmail(
                appointment.getDoctor().getEmail(),
                "New Appointment Booked",
                msg
        );
    }
    private void sendEmail(String to, String subject, String body) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}


