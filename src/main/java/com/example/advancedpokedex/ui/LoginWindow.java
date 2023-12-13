package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class LoginWindow
{
    //Needed for editing local variable in lamda
    final AtomicReference<User> usrref = new AtomicReference<>();

    public User showWindow()
    {
        User rtnUser=null;

        //Stage Setup
        Stage stage = new Stage();
        stage.setTitle("Anmeldung");
        stage.setResizable(false);

        //Scene Setup
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label lblUsername = new Label("Benutzername:");
        grid.add(lblUsername, 0, 1);
        Label lblPassword = new Label("Passwort:");
        grid.add(lblPassword, 0, 2);


        TextField userInpt = new TextField();
        grid.add(userInpt, 1, 1);

        PasswordField pwInbt = new PasswordField();
        grid.add(pwInbt, 1, 2);


        HBox buttonbox = new HBox(10);
        Button btnOK = new Button("OK");

        btnOK.setOnAction((event)->
        {
           //Source: https://stackoverflow.com/questions/34865383/variable-used-in-lambda-expression-should-be-final-or-effectively-final
           usrref.set(new User(0,userInpt.getText(),pwInbt.getText()));
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction((event)-> stage.close());


        buttonbox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonbox.getChildren().add(btnOK);
        buttonbox.getChildren().add(btnCancel);
        grid.add(buttonbox, 1, 4);


        //Finalize Stage Setup
        stage.setScene(new Scene(grid));
        stage.show();

        return rtnUser;
    }
}
