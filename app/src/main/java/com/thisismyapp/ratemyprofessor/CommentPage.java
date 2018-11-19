package com.thisismyapp.ratemyprofessor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class CommentPage extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_page);


        //This line makes it so the keyboard isn't automatically launched when opening the app
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Call character counter for the comment box:
        commentCharCounter();

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all input from boxes
                TextView userBox = (TextView) findViewById(R.id.user_name_input);
                TextView ratingBox = (TextView) findViewById(R.id.user_rating_input);
                TextView commentBox = (TextView) findViewById(R.id.commentBar);
                TextView hoursPerWeekBox = (TextView) findViewById(R.id.user_hours_per_week);

                //Getting the strings the user types into those text views
                String user = userBox.getText().toString();
                String rating = ratingBox.getText().toString();
                String comment = commentBox.getText().toString();
                String hpw = hoursPerWeekBox.getText().toString();

                //2.) Make new intent and send info back to professor page
                if(checkBoxes(user, rating, comment, hpw) == true){
                    int numRating = Integer.parseInt(rating);
                    int hoursPerWeekRating = Integer.parseInt(hpw);
                    int commentCharCount = comment.length();            //Used to get the character count of the comment
                    if(checkHoursPerWeek(hoursPerWeekRating) == false){
                        createPopUp("Check Hours Per Week", "Please make sure the hours per week entered is between 1 and 40.");
                    } else if (checkUserRating(numRating) == false){
                        createPopUp("Check Rating", "Please make sure the rating entered is between 1 and 10.");
                    } else if(commentCharCount >= 200){
                        createPopUp("Check Comment", "Please make sure your comment is 200 characters or less");
                    } else {
                        userBox.setText("");
                        ratingBox.setText("");
                        commentBox.setText("");
                        hoursPerWeekBox.setText("");

                        //If all checks pass, pass all info back to be made into a comment and added to that professor and his page
                        Intent intent = new Intent();
                        intent.putExtra("userName", user);
                        intent.putExtra("userRating", rating);
                        intent.putExtra("comment", comment);
                        intent.putExtra("hpw", hpw);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        //prof.addComment(user, rating, prof.getProfClass(), comment, hoursPerWeekRating);
                        //profPage.addComment();
                    }
                } else {
                    createPopUp("Check Text Fields", "Please make sure no text fields are left empty.");
                }
            }
        });
        //submit.setOnClickListener(new SubmitListener(profPage, prof, this));

        //Send user back to professor page after comment has been submitted correctly:

    }

    /*
     * Description: This function checks that the inputted users rating is within the valid range
     *
     * Input: (Int Users Rating)
     * Output: True or False depending on if the users is from 1 - 10
     */
    public boolean checkUserRating(int rating){
        if (rating >= 1 && rating <= 10){
            return true;
        } else {
            return false;
        }
    }

    /*
     * Description: This function returns true if the hours per week they typed in is between 1 and 40, false otherwise
     *
     * Input: (int the number of hours per week)
     * Output: true/false
     *
     */
    public boolean checkHoursPerWeek(int userInput){
        if (userInput >= 1 && userInput <= 40){
            return true;
        } else {
            return false;
        }
    }

    /*
     * Description: This function takes in a title string, and a message string and creates a pop-up box that
     *               will display the pop up with the specified title and message
     * Input: (String titleMessage, String actualMessage)
     * Output: Displays message with the specified title and message when this function is called
     */
    public void createPopUp(String title, String message){
        //Alert Dialog creates the popup to tell user to double check their input
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setCancelable(true);
        prompt.setTitle(title);
        prompt.setMessage(message);
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        prompt.show();
    }

    /*
     * Description: This function returns true if all input boxes have input in them, false otherwise
     *
     * Input: (String usersName, String usersRating, String usersComment, String hoursPerWeek)
     * Output: True or False
     */
    public boolean checkBoxes(String usersName, String usersRating, String usersComment, String hpw){
        if (usersName.equals("") || usersRating.equals("") || usersComment.equals("") || hpw.equals("")){
            return false;
        } else {
            return true;
        }
    }

    /*
     * Description: Counts the characters that the user types into the comment box (since the max allowed is 200 characters)
     *
     * Input: None
     * Output: Updates the "Character Counter" label under the comment box in real time
     */
    public void commentCharCounter(){
        //Getting text view
        final TextView charCountLabel = (TextView) findViewById(R.id.char_count);
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                charCountLabel.setText("Character Count: " + String.valueOf(s.length()) + "/200");
            }

            public void afterTextChanged(Editable s) {
            }
        };
        //Add listener to the comment box:
        EditText commentBox = (EditText) findViewById(R.id.commentBar);
        commentBox.addTextChangedListener(mTextEditorWatcher);
    }
}