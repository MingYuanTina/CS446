package cs446.budgetme.Fragement.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.budgetme.Model.User;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GroupContent {


    public static final ArrayList<String> ITEMS = new ArrayList<String>();

    private static void addItem(String item) {
        ITEMS.add(item);
    }

    private static GroupItem createGroupItem(int position) {
        return new GroupItem(String.valueOf(position), ITEMS.get(position), makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class GroupItem {
        public final String id;
        public final String content;
        public final String details;

        public GroupItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
