package pineapple.bd.com.pineapple.account.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import pineapple.bd.com.pineapple.MainActivity;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.account.AccountService;
import pineapple.bd.com.pineapple.db.GreenDaoUtils;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.db.UserAuthDao;
import pineapple.bd.com.pineapple.utils.BasicUtils;
import pineapple.bd.com.pineapple.utils.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View mFragmentLayout;
    private Button mBtnCommit;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentLayout = inflater.inflate(R.layout.fragment_button, container, false);
        mBtnCommit = (Button) mFragmentLayout.findViewById(R.id.commit);
        mBtnCommit.setText(R.string.btn_login_text);
        resumeAuth();
        return mFragmentLayout;
    }

    /**
     * 回显用户名密码
     */
    private void resumeAuth() {
        List<UserAuth> userAuths = GreenDaoUtils.getSession().getUserAuthDao().queryBuilder().orderDesc(UserAuthDao.Properties.Update_time).list();
        if(null!=userAuths&&userAuths.size()!=0){
            ((AuthActivity)getActivity()).setAuth(userAuths.get(0));
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    //activity 实现 OnFragmentInteractionListener 接口，在点击时手动去调用此方法，通知fragment改变！
    public void onActivityNotify(Bundle intentBundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(intentBundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCommit();
    }

    /**
     * 提交表单
     */
    private void onCommit() {
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAuth uAuth;
                if(null!= (uAuth = ((AuthActivity)getActivity()).getAuth())){
                    ((AuthActivity)getActivity()).showProgress();
                    AccountService.getInstance().login(getActivity(), uAuth.getIdentity_type(), uAuth.getIdentify_unique_id(), uAuth.getCredential(), new AccountService.Callback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getActivity(), R.string.login_success_tip, Toast.LENGTH_SHORT).show();
                            BasicUtils.sendIntent(getActivity(), MainActivity.class);
                            ((AuthActivity)getActivity()).hideProgress();
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure() {
                            ((AuthActivity)getActivity()).hideProgress();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
