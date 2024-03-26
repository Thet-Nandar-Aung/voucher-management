package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IFeedStrategy;

@Component
public class EmailStrategy implements IFeedStrategy {

    @Override
    public void sendFeed(Campaign campaign) {
    }

}