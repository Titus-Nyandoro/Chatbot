package vua.inc.chatbot.controller.web;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import vua.inc.chatbot.service.VuaChatService;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed
     *            bean.
     */

    @Autowired
    private VuaChatService vuaChatService;

    private Div chatMessages;
    private TextField userInput;
    private Button sendButton;
    private Button sessionButton;
    private boolean sessionActive = false;

    private List<String> chatContext;



    public MainView() {
        chatContext = new ArrayList<>();

        setAlignItems(Alignment.CENTER);

        chatMessages = new Div();
        chatMessages.addClassName("chat-messages");

        userInput = new TextField();
        userInput.setPlaceholder("Type your message...");
        userInput.setVisible(false);

        sendButton = new Button("Send");
        sendButton.setVisible(false);
        sendButton.addClickListener(e -> sendMessage(userInput.getValue()));
        sendButton.addClickShortcut(Key.ENTER);

        sessionButton = new Button("Start Session");
        sessionButton.addClickListener(e -> toggleSession());

        add(chatMessages, userInput, sendButton, sessionButton);
    }

    private void toggleSession() {
        if (!sessionActive) {
            startSession();
        } else {
            resetSession();
        }
    }

    private void startSession() {
        chatContext.clear();
        chatMessages.setText("");
        chatMessages.add(new Div("Session started. You can begin chatting."));
        sessionActive = true;
        sessionButton.setText("Reset Session");
        userInput.setVisible(true);
        sendButton.setVisible(true);
    }

    private void resetSession() {
        chatContext.clear();
        chatMessages.setText("");
        chatMessages.add(new Div("Session reset. You can begin again."));
        sessionActive = false;
        sessionButton.setText("Start Session");
        userInput.setVisible(false);
        sendButton.setVisible(false);
    }

    private void sendMessage(String message) {
        chatContext.add("You: " + message);
        chatMessages.add(createMessageDiv("You: " + message, "user", "dummy_avatar.png"));
        // Logic to process user message and generate system response
        chatMessages.add(createLoadingMessage());
       
        String systemResponse = generateSystemResponse(message);
        chatMessages.remove(chatMessages.getComponentAt(chatMessages.getComponentCount() - 1));
        chatContext.add("VUA: " + systemResponse);
        chatMessages.add(createMessageDiv("VUA: " + systemResponse, "system", "system_logo.png"));
    }

    private Div createMessageDiv(String message, String messageType, String image) {
        Div div = new Div();
        div.setText(message);
        if (messageType.equals("user")) {
            div.addClassName("user-message");
            div.getStyle().set("background-image", "url(" + image + ")");
        } else {
            div.addClassName("system-message");
            div.getStyle().set("background-image", "url(" + image + ")");
        }
        return div;
    }

    private Div createLoadingMessage() {
        Div loadingDiv = new Div();
        loadingDiv.setText("Loading...");
        loadingDiv.addClassName("system-message");
        return loadingDiv;
    }

    // Example method to generate system response
    private String generateSystemResponse(String userMessage) {
        // Replace this with your logic to generate system response based on user input
        return vuaChatService.askQuestion(userMessage);
    }
}