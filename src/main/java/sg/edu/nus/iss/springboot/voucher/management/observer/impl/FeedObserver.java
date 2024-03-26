package sg.edu.nus.iss.springboot.voucher.management.observer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.factory.FeedStrategy;
import sg.edu.nus.iss.springboot.voucher.management.factory.FeedStrategyFactory;
import sg.edu.nus.iss.springboot.voucher.management.observer.IFeedObserver;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.EmailStrategy;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.NotificationStrategy;

@Component
public class FeedObserver implements IFeedObserver {

    @Autowired
    private FeedStrategyFactory feedStrategyFactory;

    @Override
    public void update(Campaign campaign) {
        NotificationStrategy notificationStrategy = (NotificationStrategy) feedStrategyFactory
                .createStrategy(FeedStrategy.NOTIFICATION);
        notificationStrategy.sendFeed(campaign);
        EmailStrategy emailStrategy = (EmailStrategy) feedStrategyFactory.createStrategy(FeedStrategy.EMEIL);
        emailStrategy.sendFeed(campaign);
    }

}