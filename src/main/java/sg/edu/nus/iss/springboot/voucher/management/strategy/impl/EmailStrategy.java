package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IFeedStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.AmazonSES;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

@Component
public class EmailStrategy implements IFeedStrategy {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStrategy.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VourcherManagementSecurityConfig securityConfig;

    @Override
    public void sendFeed(Campaign campaign) {

        try {
            AmazonSimpleEmailService client = securityConfig.sesClient();
            String from = securityConfig.getEmailFrom().trim();
            String clientURL = securityConfig.getFrontEndUrl().trim();

            // List of users for sending feed for only customers, to be optimised for using
            // criteria, etc.
            List<User> userList = userRepository.findByRoleAndIsActiveAndIsVerifiedUsers(RoleType.CUSTOMER, true, true);
            if (!userList.isEmpty()) {
                Iterator<User> userItr = userList.iterator();
                while (userItr.hasNext()) {
                    User user = userItr.next();
                    String campaignURL = clientURL + "/components/customer/campaigns";
                    logger.info("campaignURL... {}", campaignURL);

                    String subject = "Please campaign: " + campaign.getDescription();
                    String body = "Dear [[name]],<br><br>" + "Thank you for choosing our service.<br>"
                            + "Please check out this campaign :<br>"
                            + "<h3><a href=\"[[URL]]\" target=\"_self\">[[campaign]]</a></h3>" + "Thank you"
                            + "<br><br>"
                            + "<i>(This is an auto-generated email, please do not reply)</i>";

                    body = body.replace("[[name]]", user.getUsername());
                    body = body.replace("[[campaign]]", campaign.getCampaignId());
                    body = body.replace("[[URL]]", campaignURL);

                    AmazonSES.sendEmail(client, from, Arrays.asList(user.getEmail()), subject, body);
                    logger.info(
                            "Email notification for Campaign id: " + campaign.getCampaignId() + " sent to user " +
                                    user.getEmail() + ".");
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while sendVerificationEmail, " + e.toString());
            e.printStackTrace();
        }
    }

}