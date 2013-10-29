package hsw.vkapiapp4.loaders;

import android.database.MatrixCursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hsw.vkapiapp4.vk.VkProfile;

public class VkProfilesCursor extends MatrixCursor {
    /**
     * Constructs a new cursor.
     *
     * @param columnNames names of the columns, the ordering of which
     *                    determines column ordering elsewhere in this cursor
     */
    public VkProfilesCursor(String[] columnNames) {
        super(columnNames);
        Log.d("VkLoader", "cursor columnNames: " + Arrays.toString(columnNames));
    }

    public void fill(final List<VkProfile> profiles) {
        final String[] columnNames = getColumnNames();
        final int columnNamesCount = columnNames.length;
        final ArrayList<String> row = new ArrayList<String>(columnNamesCount);
        Log.d("VkLoader", "cursor fill " + profiles.size() + " profiles");
        for (VkProfile profile : profiles) {
            row.clear();

            for (int i = 0; i < columnNamesCount; i++) {
                final String colName = columnNames[i];
                String val;
                if (colName == "_ID") {
                    val = Integer.toString(profile.id);
                } else if (colName == "full_name") {
                    val = profile.full_name();
                } else {
                    val = String.valueOf(profile.get(colName));
                }
                row.add(val);
            }

            Log.d("VkLoader", "cursor addRow: " + row);
            addRow(row);
        }
    }
}
