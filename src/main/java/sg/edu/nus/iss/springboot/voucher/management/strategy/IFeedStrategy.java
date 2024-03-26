package sg.edu.nus.iss.springboot.voucher.management.strategy;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

public interface IFeedStrategy {
    void sendFeed(Campaign campaign);
}