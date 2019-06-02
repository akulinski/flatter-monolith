package com.flatter.server.web.rest;

import com.flatter.server.config.Constants;
import com.flatter.server.domain.Offer;
import com.flatter.server.domain.ProfilePicture;
import com.flatter.server.domain.Review;
import com.flatter.server.domain.User;
import com.flatter.server.domain.dto.ProfileWithReviewsDTO;
import com.flatter.server.repository.ProfilePictureRepository;
import com.flatter.server.repository.ReviewRepository;
import com.flatter.server.repository.UserRepository;
import com.flatter.server.security.AuthoritiesConstants;
import com.flatter.server.service.MailService;
import com.flatter.server.service.MatchingService;
import com.flatter.server.service.UserService;
import com.flatter.server.service.dto.UserDTO;
import com.flatter.server.web.rest.errors.BadRequestAlertException;
import com.flatter.server.web.rest.errors.EmailAlreadyUsedException;
import com.flatter.server.web.rest.errors.LoginAlreadyUsedException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final MatchingService matchingService;

    private final ProfilePictureRepository profilePictureRepository;

    private final ReviewRepository reviewRepository;

    @Autowired
    public UserResource(UserService userService, UserRepository userRepository, MailService mailService, MatchingService matchingService, ProfilePictureRepository profilePictureRepository, ReviewRepository reviewRepository) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.matchingService = matchingService;
        this.profilePictureRepository = profilePictureRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "A user is deleted with identifier " + login, login)).build();
    }

    /**
     * Returns offers targeted to specific user
     *
     * @param principal
     * @return
     * @throws IllegalAccessException
     */
    @GetMapping("/users/offers")
    public ResponseEntity<List<Offer>> getOffersForUsers() throws IllegalAccessException {
        return new ResponseEntity<>(matchingService.getMockOffers(), HttpStatus.OK);
    }

    /**
     * @param principal
     * @return
     * @throws IllegalAccessException
     */
    @GetMapping("/users/my-profile")
    public ResponseEntity<User> getMyProfile(Principal principal) throws IllegalAccessException {
        String username = principal.getName();

        User user = userRepository.findOneByLogin(username).orElseThrow(IllegalAccessException::new);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Returns profile with reviews
     *
     * @param principal
     * @return
     * @throws IllegalAccessException
     */
    @GetMapping("/users/my-profile-with-reviews")
    public ResponseEntity<ProfileWithReviewsDTO> getMyProfileWithReviews(Principal principal) throws IllegalAccessException {
        String username = principal.getName();

        User user = userRepository.findOneByLogin(username).orElseThrow(IllegalAccessException::new);

        ProfileWithReviewsDTO profileWithReviewsDTO = new ProfileWithReviewsDTO();

        profileWithReviewsDTO.setReceiver(user);
        profileWithReviewsDTO.setProfilePicture(profilePictureRepository.findAllByUser(user).orElseThrow(() -> new IllegalStateException("User not found " + username)));

        LinkedList<Review> allByReceiver = reviewRepository.getAllByReceiver(user);
        LinkedList<Pair<Review, ProfilePicture>> pairLinkedList = new LinkedList<>();

        allByReceiver.forEach(review -> pairLinkedList.add(new Pair<>(review, profilePictureRepository.findAllByUser(review.getIssuer()).get())));
        profileWithReviewsDTO.setPairs(pairLinkedList);

        return new ResponseEntity<>(profileWithReviewsDTO, HttpStatus.OK);
    }

}
