package hsw.vkapiapp4.loaders;

import android.database.MatrixCursor;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import hsw.vkapiapp4.vk.VkProfile;

public class VkProfilesCursor extends MatrixCursor {
    static final String LOG_TAG = "VkLoader cursor";

    /**
     * Constructs a new cursor.
     *
     * @param columnNames names of the columns, the ordering of which
     *                    determines column ordering elsewhere in this cursor
     */
    public VkProfilesCursor(String[] columnNames) {
        super(columnNames);
        Log.d(LOG_TAG, "cursor columnNames: " + Arrays.toString(columnNames));
    }

    public VkProfilesCursor(String[] columnNames, int initialCapacity) {
        super(columnNames, initialCapacity);
        Log.d(LOG_TAG, "cursor columnNames: " + Arrays.toString(columnNames));
    }

    public void fill(final List<VkProfile> profiles) {
        final String[] columnNames = getColumnNames();
        final int columnNamesCount = columnNames.length;
        final Object[] row = new Object[columnNamesCount];
        Log.d(LOG_TAG, "cursor fill " + profiles.size() + " profiles");
        for (VkProfile profile : profiles) {
            for (int i = 0; i < columnNamesCount; i++) {
                final String colName = columnNames[i];
                if (colName.equals("_ID")) {
                    row[i] = profile.id;
                } else if (colName.equals("full_name")) {
                    row[i] = profile.full_name();
                } else {
                    row[i] = profile.get(colName);
                }
            }

            Log.d(LOG_TAG, "cursor addRow: " + Arrays.toString(row));
            addRow(row);
        }
    }

    @Override
    public int getType(int column) {
        Log.d(LOG_TAG, "cursor getType " + column);
        return super.getType(column);
    }
}
