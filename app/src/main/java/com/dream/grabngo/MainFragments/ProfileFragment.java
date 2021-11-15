package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.Activities.GetStartedActivity;
import com.dream.grabngo.ProfileSubFragments.EditProfileFragment;
import com.dream.grabngo.ProfileSubFragments.HistoryFragment;
import com.dream.grabngo.ProfileSubFragments.NotificationsFragment;
import com.dream.grabngo.ProfileSubFragments.RatingsFragment;
import com.dream.grabngo.ProfileSubFragments.SettingsFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.ImageLoadTask;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.utils.SingletonClass;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private final FragmentManager supportFragmentManager;
    private final Context context;
    private TextView userName, emailID;
    private ImageView profile_image;
    private LinearLayout historyButton, settingsButton, ratingsButton, supportButton;
    private RelativeLayout editProfileButton;
    private CardView logoutButton;
    private AlertDialog dialog;
    private JSONObject userDetails;

    public ProfileFragment(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppID.getInstance().initialize(requireContext(), TENANT_ID, REGION);
        userDetails = SharedPrefConfig.readUserDetails(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = groupFragmentView.findViewById(R.id.user_name_text_view);
        emailID = groupFragmentView.findViewById(R.id.user_emailID_text_view);
        profile_image = groupFragmentView.findViewById(R.id.profile_image);
//        notificationsButton = groupFragmentView.findViewById(R.id.profile_notifications_button);
        historyButton = groupFragmentView.findViewById(R.id.profile_history_button);
        settingsButton = groupFragmentView.findViewById(R.id.profile_settings_button);
        ratingsButton = groupFragmentView.findViewById(R.id.profile_ratings_button);
        supportButton = groupFragmentView.findViewById(R.id.profile_support_button);
        editProfileButton = groupFragmentView.findViewById(R.id.profile_edit_button);
        logoutButton = groupFragmentView.findViewById(R.id.profile_logout_button);

        SingletonClass singleToneClass = SingletonClass.getInstance();
        IdentityToken identityToken = singleToneClass.getIdentityToken();

        new ImageLoadTask(getContext(), identityToken.getPicture(), profile_image).execute();

        try {
            userName.setText(userDetails.getString("CUSTOMER_NAME"));
            emailID.setText(userDetails.getString("EMAIL_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
         * Working with child fragments
         */
        editProfileButton.setOnClickListener(v -> handleFragment(editProfileButton.getId()));

        historyButton.setOnClickListener(v -> handleFragment(historyButton.getId()));

        settingsButton.setOnClickListener(v -> handleFragment(settingsButton.getId()));

        ratingsButton.setOnClickListener(v -> handleFragment(ratingsButton.getId()));

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sparkInc@gmail.com"});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, "Grab N Go - Support"));
            }
        });

        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View view = getLayoutInflater().inflate(R.layout.dialog_logout_confirmation, null);
            builder.setView(view);

            view.findViewById(R.id.yes_quit_button).setOnClickListener(view1 -> {
                SharedPrefConfig.writeIsLoggedIn(requireContext(), false);
                SharedPrefConfig.writeAreDetailsGiven(requireContext(), false);
                SharedPrefConfig.writeShopWiseCartItems(requireContext(), new ArrayList<>());
                AppID.getInstance().logout();
                dialog.cancel();
                startActivity(new Intent(requireActivity(), GetStartedActivity.class));
                requireActivity().finish();
            });

            view.findViewById(R.id.no_stay_button).setOnClickListener(view2 -> dialog.cancel());

            dialog = builder.create();
            dialog.show();
        });

        return groupFragmentView;
    }

    @SuppressLint("NonConstantResourceId")
    private void handleFragment(int id) {
        Fragment fragment = null;
        switch (id) {
//            case R.id.profile_notifications_button:
//                fragment = new NotificationsFragment(context, supportFragmentManager);
//                break;
            case R.id.profile_edit_button:
                fragment = new EditProfileFragment(context, supportFragmentManager);
                break;
            case R.id.profile_history_button:
                fragment = new HistoryFragment(context, supportFragmentManager);
                break;
            case R.id.profile_settings_button:
                fragment = new SettingsFragment(context, supportFragmentManager);
                break;
            case R.id.profile_ratings_button:
                fragment = new RatingsFragment(context, supportFragmentManager);
                break;
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, Objects.requireNonNull(fragment)).commit();
    }
}