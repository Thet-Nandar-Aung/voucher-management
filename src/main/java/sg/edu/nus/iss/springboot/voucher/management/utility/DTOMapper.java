package sg.edu.nus.iss.springboot.voucher.management.utility;

import org.springframework.stereotype.Component;

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

@Component
public class DTOMapper {

	public static CampaignDTO toCampaignDTO(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO();
		campaignDTO.setCampaignId(campaign.getCampaignId());
		campaignDTO.setDescription(campaign.getDescription());
		campaignDTO.setStore(toStoreDTO(campaign.getStore()));
		campaignDTO.setCampaignStatus(campaign.getCampaignStatus());
		campaignDTO.setTagsJson(campaign.getTagsJson());
		campaignDTO.setNumberOfVouchers(campaign.getNumberOfVouchers());
		campaignDTO.setNumberOfLikes(campaign.getNumberOfLikes());
		campaignDTO.setPin(campaign.getPin());
		campaignDTO.setTandc(campaign.getTandc());
		campaignDTO.setAmount(campaign.getAmount());
		campaignDTO.setStartDate(campaign.getStartDate());
		campaignDTO.setEndDate(campaign.getEndDate());
		if (campaign.getCreatedBy() != null) {
			campaignDTO.setCreatedBy(toUserDTO(campaign.getCreatedBy()));
		}
		campaignDTO.setCreatedDate(campaign.getCreatedDate());
		if (campaign.getUpdatedBy() != null) {
			campaignDTO.setUpdatedBy(toUserDTO(campaign.getUpdatedBy()));
			campaignDTO.setUpdatedDate(campaign.getUpdatedDate());
		}
		if (campaign.getVoucher() != null) {
			campaignDTO.setNumberOfClaimedVouchers(campaign.getVoucher().size());
		}
		return campaignDTO;
	}

	public static UserDTO toUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setRole(user.getRole());
		userDTO.setActive(user.isActive());
		userDTO.setLastLoginDate(user.getLastLoginDate());
		userDTO.setCreatedDate(user.getCreatedDate());
		userDTO.setUpdatedDate(user.getUpdatedDate());
		userDTO.setVerified(user.isVerified());
		return userDTO;
	}

	public static StoreDTO toStoreDTO(Store store) {
		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setStoreId(store.getStoreId());
		storeDTO.setStoreName(store.getStoreName());
		storeDTO.setDescription(store.getDescription());
		storeDTO.setImage(store.getImage());
		storeDTO.setTagsJson(store.getTagsJson());
		storeDTO.setAddress1(store.getAddress1());
		storeDTO.setAddress2(store.getAddress2());
		storeDTO.setAddress3(store.getAddress3());
		storeDTO.setPostalCode(store.getPostalCode());
		storeDTO.setCity(store.getCity());
		storeDTO.setState(store.getState());
		storeDTO.setCountry(store.getCountry());
		storeDTO.setContactNumber(store.getContactNumber());
		storeDTO.setCreatedDate(store.getCreatedDate());
		if (store.getCreatedBy() != null) {
			storeDTO.setCreatedBy(toUserDTO(store.getCreatedBy()));
		}
		storeDTO.setUpdatedDate(store.getUpdatedDate());
		if (store.getUpdatedBy() != null) {
			storeDTO.setUpdatedBy(toUserDTO(store.getUpdatedBy()));
		}
		return storeDTO;
	}

	public static VoucherDTO toVoucherDTO(Voucher voucher) {
		VoucherDTO voucherDTO = new VoucherDTO();
		voucherDTO.setVoucherId(voucher.getVoucherId());
		voucherDTO.setCampaign(toCampaignDTO(voucher.getCampaign()));
		voucherDTO.setVoucherStatus(voucher.getVoucherStatus());
		voucherDTO.setClaimTime(voucher.getClaimTime());
		voucherDTO.setConsumedTime(voucher.getConsumedTime());
		voucherDTO.setClaimedBy(toUserDTO(voucher.getClaimedBy()));
		return voucherDTO;
	}

	public static FeedDTO toFeedDTO(Feed feed) {
		FeedDTO feedDTO = new FeedDTO();
		feedDTO.setCampaign(toCampaignDTO(feed.getCampaignId()));
		feedDTO.setFeedId(feed.getFeedId());
		feedDTO.setRead(feed.isRead());
		feedDTO.setReadTime(feed.getReadTime());
		feedDTO.setTargetUserId(toUserDTO(feed.getTargetUserId()));

		return feedDTO;
	}

	public static StoreDTO mapStoreToResult(Store store) {
		StoreDTO storeDTO = new StoreDTO();
		storeDTO.setStoreId(store.getStoreId());
		storeDTO.setStoreName(store.getStoreName());
		storeDTO.setDescription(GeneralUtility.makeNotNull(store.getDescription()));

		String address = GeneralUtility.makeNotNull(store.getAddress1()).trim();
		address += address.isEmpty() ? "" : ", " + GeneralUtility.makeNotNull(store.getAddress2()).trim();
		address += address.isEmpty() ? "" : ", " + GeneralUtility.makeNotNull(store.getAddress3()).trim();
		address += address.isEmpty() ? "" : ", " + GeneralUtility.makeNotNull(store.getPostalCode());

		storeDTO.setAddress(GeneralUtility.makeNotNull(address));
		storeDTO.setAddress1(store.getAddress1());
		storeDTO.setAddress2(store.getAddress2());
		storeDTO.setAddress3(store.getAddress3());
		storeDTO.setCity(GeneralUtility.makeNotNull(store.getCity()));
		storeDTO.setState(GeneralUtility.makeNotNull(store.getState()));
		storeDTO.setCountry(GeneralUtility.makeNotNull(store.getCountry()));
		storeDTO.setContactNumber(GeneralUtility.makeNotNull(store.getContactNumber()));
		storeDTO.setPostalCode(GeneralUtility.makeNotNull(store.getPostalCode()));
		storeDTO.setImage(GeneralUtility.makeNotNull(store.getImage()));

		storeDTO.setCreatedDate(store.getCreatedDate());
		if (store.getCreatedBy() != null) {
			storeDTO.setCreatedBy(toUserDTO(store.getCreatedBy()));
		}
		storeDTO.setUpdatedDate(store.getUpdatedDate());
		if (store.getUpdatedBy() != null) {
			storeDTO.setUpdatedBy(toUserDTO(store.getUpdatedBy()));
		}
		return storeDTO;
	}

}
