package sg.edu.nus.iss.springboot.voucher.management.observer;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

public interface IFeedObserver {

    void update(Campaign campaign);

}