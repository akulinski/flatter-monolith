package com.flatter.server.web.rest;

import com.flatter.server.domain.Conversation;
import com.flatter.server.domain.Message;
import com.flatter.server.repository.ConversationRepository;
import com.flatter.server.repository.MessageRepository;
import com.flatter.server.repository.UserRepository;
import com.flatter.server.service.kafka.MessageProducerChannel;
import com.flatter.server.web.rest.errors.BadRequestAlertException;
import com.flatter.server.web.rest.util.HeaderUtil;
import domain.MessageDTO;
import domain.events.MessageSentEvent;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    public static final String NO_USER_FOUND_WITH_NAME = "No user found with name {}";
    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    private final MessageRepository messageRepository;

    private final ConversationRepository conversationRepository;

    private final UserRepository userRepository;

    private final MessageChannel messageChannel;

    public MessageResource(MessageRepository messageRepository, ConversationRepository conversationRepository, UserRepository userRepository, MessageProducerChannel messageProducerChannel) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.messageChannel = messageProducerChannel.messageChannel();
    }

    /**
     * POST  /messages : Create a new message.
     *
     * @param message the message to create
     * @return the ResponseEntity with status 201 (Created) and with body the new message, or with status 400 (Bad Request) if the message has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);

        if (message.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Message result = messageRepository.save(message);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/send-message")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO messageDTO) {

        log.debug("Sending message: FROM {} TO {}", messageDTO.getSender(), messageDTO.getReceiver());
        Optional<Conversation> conversationOptional = conversationRepository.findAllBySender_LoginAndReciver_Login(messageDTO.getSender(), messageDTO.getReceiver());
        Message message = getMessageFromDTO(messageDTO);

        if (conversationOptional.isPresent()) {
            message.setConversation(conversationOptional.get());

        } else {
            Conversation conversation = getConversationFromDTOAndSetSenders(messageDTO);
            message.setConversation(conversation);
        }
        messageRepository.save(message);

        MessageSentEvent messageSentEvent = new MessageSentEvent();
        messageSentEvent.setMessageDTO(messageDTO);

        messageChannel.send(MessageBuilder.withPayload(messageSentEvent).build());

        return ResponseEntity.ok(message);
    }

    private Conversation getConversationFromDTOAndSetSenders(@RequestBody MessageDTO messageDTO) {
        Conversation conversation = new Conversation();
        conversation.setSender(userRepository.findOneByLogin(messageDTO.getSender()).orElseThrow(() -> new IllegalArgumentException(String.format(NO_USER_FOUND_WITH_NAME, messageDTO.getSender()))));
        conversation.setReciver(userRepository.findOneByLogin(messageDTO.getReceiver()).orElseThrow(() -> new IllegalArgumentException(String.format(NO_USER_FOUND_WITH_NAME, messageDTO.getReceiver()))));
        conversationRepository.save(conversation);
        return conversation;
    }

    private Message getMessageFromDTO(@RequestBody MessageDTO messageDTO) {
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setIsSeen(false);
        message.setCreationDate(messageDTO.getCreationDate());
        return message;
    }

    /**
     * PUT  /messages : Updates an existing message.
     *
     * @param message the message to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated message,
     * or with status 400 (Bad Request) if the message is not valid,
     * or with status 500 (Internal Server Error) if the message couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/messages")
    public ResponseEntity<Message> updateMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Message result = messageRepository.save(message);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, message.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messages : get all the messages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of messages in body
     */
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        log.debug("REST request to get all Messages");
        return messageRepository.findAll();
    }

    /**
     * GET  /messages/:id : get the "id" message.
     *
     * @param id the id of the message to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the message, or with status 404 (Not Found)
     */
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        Optional<Message> message = messageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(message);
    }

    /**
     * DELETE  /messages/:id : delete the "id" message.
     *
     * @param id the id of the message to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
