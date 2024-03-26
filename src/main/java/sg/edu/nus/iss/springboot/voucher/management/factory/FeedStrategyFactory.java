package sg.edu.nus.iss.springboot.voucher.management.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.EmailStrategy;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IFeedStrategy;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.NotificationStrategy;

@Component
public class FeedStrategyFactory {

    @Autowired
    private EmailStrategy emailStrategy;

    @Autowired
    private NotificationStrategy notificationStrategy;

    public IFeedStrategy createStrategy(FeedStrategy type) {
        if (FeedStrategy.EMEIL.equals(type)) {
            return emailStrategy;
        }
        if (FeedStrategy.NOTIFICATION.equals(type)) {
            return notificationStrategy;
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }
}