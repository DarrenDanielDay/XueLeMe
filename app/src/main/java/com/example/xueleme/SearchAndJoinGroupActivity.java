package com.example.xueleme;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.chatgroup.JoinGroupForm;
import com.example.xueleme.models.responses.BriefGroup;
import com.example.xueleme.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchAndJoinGroupActivity extends BaseActivity {

    IChatGroupController chatGroupController = new ChatGroupController();
    IAccountController accountController;
    private final List<String> groupNames = new ArrayList<>();
    private final List<BriefGroup> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountController = new AccountController(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groupNames);
        setContentView(R.layout.activity_search_and_join_group);
        Button searchButton = findViewById(R.id.search_group_button);
        TextView searchText = findViewById(R.id.search_group_text);
        ListView groupListView = findViewById(R.id.group_list);
        groupListView.setAdapter(arrayAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = searchText.getText().toString();
                chatGroupController.searchGroupsByName(new UserAction<>(searchName, new ActionResultHandler<List<BriefGroup>, String>() {
                    @Override
                    public void onSuccess(List<BriefGroup> briefGroups) {
                        groupNames.clear();
                        groups.clear();
                        for (BriefGroup briefGroup : briefGroups) {
                            groupNames.add(briefGroup.name);
                            groups.add(briefGroup);
                        }
                        runOnUiThread(() -> arrayAdapter.notifyDataSetChanged());
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(SearchAndJoinGroupActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
            }
        });
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchAndJoinGroupActivity.this);
                dialog.setMessage("确认加群");
                dialog.setMessage("确认申请加入群聊 " + groupNames.get(position) + " (群号:" + groups.get(position).id + ")吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JoinGroupForm form = new JoinGroupForm();
                        form.groupId = groups.get(position).id;
                        form.userId = accountController.getCurrentUser().id;
                        chatGroupController.joinGroup(new UserAction<>(form, new ActionResultHandler<String, String>() {
                            @Override
                            public void onSuccess(String s) {
                                ToastHelper.showToastWithLooper(SearchAndJoinGroupActivity.this, s);
                            }

                            @Override
                            public void onError(String s) {
                                ToastHelper.showToastWithLooper(SearchAndJoinGroupActivity.this, s);
                            }
                        }));
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }
}