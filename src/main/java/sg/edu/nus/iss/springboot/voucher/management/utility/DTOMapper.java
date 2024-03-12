package sg.edu.nus.iss.springboot.voucher.management.utility;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public class DTOMapper {
    
    public static CampaignDTO toCampaignDTO(Campaign campaign){
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignId(campaign.getCampaignId());
        campaignDTO.setDescription(campaign.getDescription());
        campaignDTO.setStore(toStoreDTO(campaign.getStore()));
        campaignDTO.setCampaignStatus(campaign.getCampaignStatus());
        campaignDTO.setTagsJson(campaign.getTagsJson());
        campaignDTO.setNumberOfVouchers(campaign.getNumberOfVouchers());
        campaignDTO.setNumberOfLikes(campaign.getNumberOfLikes());
        campaignDTO.setPin(campaign.getPin());
        campaignDTO.setCondition1(campaign.getCondition1());
        campaignDTO.setCondition2(campaign.getCondition2());
        campaignDTO.setStartDate(campaign.getStartDate());
        campaignDTO.setEndDate(campaign.getEndDate());
        campaignDTO.setCreatedBy(toUserDTO(campaign.getCreatedBy()));
        campaignDTO.setCreatedDate(campaign.getCreatedDate());
        campaignDTO.setUpdatedBy(toUserDTO(campaign.getUpdatedBy()));
        campaignDTO.setUpdatedDate(campaign.getUpdatedDate());
        return campaignDTO;
    }

    public static UserDTO toUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setImage(user.getImage());
        userDTO.setIsActive(user.isActive());
        userDTO.setLastLoginDate(user.getLastLoginDate());
        return userDTO;
    }

    public static StoreDTO toStoreDTO(Store store){
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setStoreName(store.getStoreName());
        return storeDTO;
    }

    public static VoucherDTO toVoucherDTO(Voucher voucher){
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherId(voucher.getVoucherId());
        voucherDTO.setCampaign(toCampaignDTO(voucher.getCampaign()));
        voucherDTO.setVoucherStatus(voucher.getCampaignStatus());
        voucherDTO.setAmount(voucher.getAmount());
        voucherDTO.setValidThrough(voucher.getValidThrough());
        voucherDTO.setClaimTime(voucher.getClaimTime());
        voucherDTO.setConsumedTime(voucher.getConsumedTime());
        voucherDTO.setClaimedBy(toUserDTO(voucher.getClaimedBy()));
        return voucherDTO;
    }
    
    public static FeedDTO toFeedDTO(Feed feed) {
    	FeedDTO feedDTO = new FeedDTO();
    	feedDTO.setCampaignId(feed.getCampaignId().getCampaignId());
    	feedDTO.setFeedId(feed.getFeedId());
    	feedDTO.setRead(feed.isRead());
    	feedDTO.setReadTime(feed.getReadTime());
    	feedDTO.setTargetUserId(toUserDTO(feed.getTargetUserId()));
    	
    	return feedDTO;
    }
}
