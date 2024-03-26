package sg.edu.nus.iss.springboot.voucher.management.observer;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.observer.impl.FeedObserver;

public interface ICampaignSubject {

    void registerObserver(FeedObserver observer);

    void removeObserver(FeedObserver observer);

    void notifyObservers(Campaign campaign);
}