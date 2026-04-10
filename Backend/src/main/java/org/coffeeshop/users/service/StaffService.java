package org.coffeeshop.users.service;

import jakarta.persistence.EntityNotFoundException;
import org.coffeeshop.Exceptions.UserExceptions.StaffServiceException;
import org.coffeeshop.users.dtos.CreateStaffDto;
import org.coffeeshop.users.dtos.StaffDto;
import org.coffeeshop.users.models.Staff;
import org.coffeeshop.users.repositories.StaffRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * Service for managing staff users in the coffee shop
 * also for implementing business logic
 *
 * */
@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    /**
    * this is the staff service Constructor
    * it takes the repository and the password encoder as parameters
    * @param repo  this is the staff repository reference which is called
     *              whenever database interaction is needed
     * @param passwordEncoder this is used to ensure the password is encoded when
     *                        the staff user is created
    * */
    public StaffService(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // create staff
    /**
     * this method handles post requests from the controller
     * @param  dto this is the data transfer object that is passed from the controller
     * @return method also returns a staff dto
     * @throws StaffServiceException if staff entity could not be created
     * */
    public StaffDto create(CreateStaffDto dto) {
        try {
            Staff staff = fromCreateDto(dto);
            Staff saved = staffRepository.save(staff);

            return toDto(saved);
        } catch (DataAccessException e) {
            throw new StaffServiceException("Could not create staff", e);
        }
    }

    /**
     * this method gets staff data by id and does it asynchronously to avoid code blocking
     * @param id is the id of the staff user
     * @return it returns a promise that the staff data will be retrieved
     * @throws StaffServiceException if error in database
     * @throws EntityNotFoundException if the staff user could not be found
     * */
    @Async
    // get staff by id
    public CompletableFuture<StaffDto> getStaffById(Long id) {
        try {
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("staff not found: " + id));
            //returns completed Future type to controller
            return CompletableFuture.completedFuture(toDto(staff));
        } catch (DataAccessException e) {
            throw new StaffServiceException("Database error while fetching staff", e);
        }
    }
    /**
     * this gets all staff data from the database
     * */

    public List<StaffDto> getAllStaff() {
        List<Staff> allStaff = staffRepository.findAll();
        return toDto(allStaff);
    }

    /**
     * this method updates the staff data
     * @param id the path id identifying the staff record to update
     * @param dto it takes a dto object as input
     * @return it returns a staffDto promise
     * @throws StaffServiceException if there was an error updating the user
     * */
    @Async
    public CompletableFuture<StaffDto> updateStaff(Long id, StaffDto dto) {
        try {
            Staff staff = fromDto(id, dto);

            Staff updatedStaff = staffRepository.save(staff);
            return CompletableFuture.completedFuture(toDto(updatedStaff));
        } catch (DataAccessException e) {
            throw new StaffServiceException("error updating user ", e);
        }
    }


    /**
     * this method deletes staff data by id
     * @param id to locate the data by staff id
     * @return  it returns a string confirmation message that user has been deleted
     * @throws StaffServiceException if there was an error deleting the user
     *
     * */
    @Async
    public CompletableFuture<String> deleteStaff(Long id) {
        try {
            staffRepository.deleteById(id);
            staffRepository.flush();
            return CompletableFuture.completedFuture("Staff deleted");
        } catch (DataAccessException e) {
            throw new StaffServiceException("Error deleting staff with id " + id, e);
        }
    }

    /**
     * this is a private helper method that converts staff objects to staff Dtos
     * password is write-only and never returned to clients
     * */
    private StaffDto toDto(Staff staff) {
        // password is write-only and never returned to clients
        return new StaffDto(
            staff.getStaffId(),
                staff.getUsername(),
                staff.getFirstName(),
                staff.getLastName(),
            staff.getRole(),
            null
        );
    }
    /**
     * this is a private helper method that converts objects of type createStaffDto
     * into staff objects
     * */
    private Staff fromCreateDto(CreateStaffDto dto) {
        return new Staff(
                dto.username(),
                dto.firstName(),
                dto.lastName(),
                dto.role(),
                passwordEncoder.encode(dto.password())
        );
    }


    /**
     * this helper method converts from a dto object to a staff object
     * uses the provided id (from path) as the authoritative record identifier
     * ensures password fields have a valid password if not it replaces the field in the builder with the existing password
     * of that user
     * */
    private Staff fromDto(Long id, StaffDto dto) {
        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("staff not found"));

        String passwordHash = existingStaff.getPasswordHash();
        if (dto.password() != null && !dto.password().isBlank()) {
            passwordHash = passwordEncoder.encode(dto.password());
        }

        return new Staff(
                existingStaff.getStaffId(),
                dto.username(),
                dto.firstName(),
                dto.lastName(),
                dto.role(),
                passwordHash
        );
    }

    private List<StaffDto> toDto(List<Staff> staff) {
        List<StaffDto> dtos = new ArrayList<>();
        for (Staff staffMember : staff) {
            dtos.add(toDto(staffMember));

        }
        return dtos;
    }



}
