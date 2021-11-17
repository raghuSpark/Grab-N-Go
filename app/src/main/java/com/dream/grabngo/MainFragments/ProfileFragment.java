package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Activities.GetStartedActivity;
import com.dream.grabngo.CustomClasses.UserDetails;
import com.dream.grabngo.ProfileSubFragments.EditProfileFragment;
import com.dream.grabngo.ProfileSubFragments.HistoryFragment;
import com.dream.grabngo.ProfileSubFragments.RatingsFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.ibm.cloud.appid.android.api.AppID;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private final FragmentManager supportFragmentManager;
    private final Context context;
    private TextView userName, emailID;
    private ImageView profileImage;
    private LinearLayout historyButton, ratingsButton, supportButton;
    private RelativeLayout editProfileButton;
    private CardView logoutButton;
    private AlertDialog dialog;
    private UserDetails userDetails;

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
        View groupFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = groupFragmentView.findViewById(R.id.user_name_text_view);
        emailID = groupFragmentView.findViewById(R.id.user_emailID_text_view);
        profileImage = groupFragmentView.findViewById(R.id.profile_image);
        historyButton = groupFragmentView.findViewById(R.id.profile_history_button);
        ratingsButton = groupFragmentView.findViewById(R.id.profile_ratings_button);
        supportButton = groupFragmentView.findViewById(R.id.profile_support_button);
        editProfileButton = groupFragmentView.findViewById(R.id.profile_edit_button);
        logoutButton = groupFragmentView.findViewById(R.id.profile_logout_button);

        userName.setText(userDetails.getCustomerName());
        emailID.setText(userDetails.getEmailId());
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_profile_active);
        userDetails.setProfileImage(icon);
        SharedPrefConfig.writeUserDetails(context,userDetails);

        /*
         * Working with child fragments
         */
        editProfileButton.setOnClickListener(v -> handleFragment(editProfileButton.getId()));

        historyButton.setOnClickListener(v -> handleFragment(historyButton.getId()));

        ratingsButton.setOnClickListener(v -> handleFragment(ratingsButton.getId()));

        supportButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sparkInc@gmail.com"});
            startActivity(Intent.createChooser(intent, "Grab N Go - Support"));
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
            case R.id.profile_edit_button:
                fragment = new EditProfileFragment(context, supportFragmentManager);
                break;
            case R.id.profile_history_button:
                fragment = new HistoryFragment(context, supportFragmentManager);
                break;
            case R.id.profile_ratings_button:
                fragment = new RatingsFragment(context, supportFragmentManager);
                break;
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, Objects.requireNonNull(fragment)).commit();
    }

    private void getProfileImage() {
        String url = "https://grabngo-api.herokuapp.com/gng/v1/get-image";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                profileImage.setImageBitmap(response);
                userDetails.setProfileImage(response);
                SharedPrefConfig.writeUserDetails(context,userDetails);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userDetails.setProfileImage(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_profile_active));
                SharedPrefConfig.writeUserDetails(context, userDetails);
                profileImage.setImageResource(R.drawable.ic_profile_active);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}