package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IStoreService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;
import sg.edu.nus.iss.springboot.voucher.management.utility.ImageUploadToS3;

@Service
public class StoreService implements IStoreService {

	private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private VourcherManagementSecurityConfig securityConfig;

	@Override
	public StoreDTO create(Store store, MultipartFile uploadFile) {

		StoreDTO storeDTO = new StoreDTO();
		try {

			User user = userRepository.findByEmail(store.getCreatedBy().getEmail());
			store = this.uploadImage(store, uploadFile);

			store.setCreatedBy(user);
			store.setCreatedDate(LocalDateTime.now());
			logger.info("Saving store...");
			Store createdStore = storeRepository.save(store);
			logger.info("Saved successfully...{}", createdStore.getStoreId());
			storeDTO = DTOMapper.toStoreDTO(createdStore);

		} catch (Exception e) {
			logger.error("Error occurred while user creating, " + e.toString());
			e.printStackTrace();

		}

		return storeDTO;
	}

	@Override
	public StoreDTO update(Store store, MultipartFile uploadFile) {
		StoreDTO storeDTO = new StoreDTO();
		try {

			Optional<Store> dbStore = storeRepository.findById(store.getStoreId());
			User user = userRepository.findByEmail(store.getUpdatedBy().getEmail());
			store = this.uploadImage(store, uploadFile);
			dbStore.get().setDescription(GeneralUtility.makeNotNull(store.getDescription()));
			dbStore.get().setAddress1(GeneralUtility.makeNotNull(store.getAddress1()));
			dbStore.get().setAddress2(GeneralUtility.makeNotNull(store.getAddress2()));
			dbStore.get().setAddress3(GeneralUtility.makeNotNull(store.getAddress3()));
			dbStore.get().setCity(GeneralUtility.makeNotNull(store.getCity()));
			dbStore.get().setState(GeneralUtility.makeNotNull(store.getState()));
			dbStore.get().setCountry(GeneralUtility.makeNotNull(store.getCountry()));
			dbStore.get().setContactNumber(GeneralUtility.makeNotNull(store.getContactNumber()));
			dbStore.get().setPostalCode(GeneralUtility.makeNotNull(store.getPostalCode()));
			dbStore.get().setDeleted(store.isDeleted());
			dbStore.get().setImage(store.getImage());
			dbStore.get().setUpdatedBy(user);
			dbStore.get().setUpdatedDate(LocalDateTime.now());

			logger.info("Updating store...");
			Store updatedStore = storeRepository.save(dbStore.get());
			logger.info("Updated successfully...{}", updatedStore.getStoreId());
			storeDTO = DTOMapper.toStoreDTO(updatedStore);

		} catch (Exception e) {
			logger.error("Error occurred while user creating, " + e.toString());
			e.printStackTrace();

		}

		return storeDTO;
	}

	@Override
	public StoreDTO findByStoreName(String storename) {
		StoreDTO storeDTO = new StoreDTO();
		try {
			Store store = storeRepository.findBystoreName(storename);
			storeDTO = DTOMapper.toStoreDTO(store);
		} catch (Exception ex) {
			logger.error("findByStoreId exception... {}", ex.toString());

		}
		return storeDTO;

	}

	@Override
	public Map<Long, List<StoreDTO>> findByIsDeletedFalse(Pageable pageable) {
		Map<Long, List<StoreDTO>> result = new HashMap<>();
		List<StoreDTO> storeDTOList = new ArrayList<>();
		try {
			Page<Store> storePages = storeRepository.findByIsDeletedFalse(pageable);
			long totalRecord = storePages.getTotalElements();
			if (totalRecord > 0) {

				for (Store store : storePages.getContent()) {
					StoreDTO storeDTO = DTOMapper.mapStoreToResult(store);
					storeDTOList.add(storeDTO);
				}
			} else {
				logger.info("Store not found...");
			}

			result.put(totalRecord, storeDTOList);
		} catch (Exception ex) {
			logger.error("findByIsDeletedFalse exception... {}", ex.toString());

		}

		return result;
	}

	@Override
	public Map<Long, List<StoreDTO>> findAllByUserAndStatus(User createdBy, boolean isDeleted, Pageable pageable) {
		Map<Long, List<StoreDTO>> result = new HashMap<>();
		List<StoreDTO> storeDTOList = new ArrayList<>();
		try {
			Page<Store> storePages = storeRepository.findAllByUserAndStatus(createdBy, isDeleted, pageable);
			long totalRecord = storePages.getTotalElements();
			if (totalRecord > 0) {

				for (Store store : storePages.getContent()) {
					StoreDTO storeDTO = DTOMapper.mapStoreToResult(store);
					storeDTOList.add(storeDTO);
				}
			} else {
				logger.info("Store not found...");
			}

			result.put(totalRecord, storeDTOList);
		} catch (Exception ex) {
			logger.error("findByIsDeletedFalse exception... {}", ex.toString());

		}

		return result;
	}

	@Override
	public StoreDTO findByStoreId(String storeId) {
		StoreDTO storeDTO = new StoreDTO();
		try {
			Optional<Store> store = storeRepository.findById(storeId);
			if (store.isPresent()) {
				storeDTO = DTOMapper.toStoreDTO(store.get());
			} else {
				logger.info("Store not found for storeId {}...", storeId);
			}
		} catch (Exception ex) {
			logger.error("findByStoreId exception... {}", ex.toString());

		}
		return storeDTO;
	}

	@Override
	public Store uploadImage(Store store, MultipartFile uploadFile) {
		try {
			if (!GeneralUtility.makeNotNull(uploadFile).equals("")) {
				logger.info("create store: " + store.getStoreName() + "::" + uploadFile.getOriginalFilename());
				if (securityConfig != null) {

					boolean isImageUploaded = ImageUploadToS3.checkImageExistBeforeUpload(s3Client, uploadFile,
							securityConfig, securityConfig.getS3ImagePublic().trim());
					if (isImageUploaded) {
						String imageUrl = securityConfig.getS3ImageUrlPrefix().trim() + "/"
								+ securityConfig.getS3ImagePublic().trim() + uploadFile.getOriginalFilename().trim();
						store.setImage(imageUrl);
					}
				}

			}
		} catch (Exception e) {
			logger.error("Error occurred while uploading Image, " + e.toString());
			e.printStackTrace();

		}
		return store;
	}

}
