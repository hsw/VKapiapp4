package hsw.vkapiapp4.loaders;

import android.database.MatrixCursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    }

    public void fill(final List<VkProfile> profiles) {
        final String[] columnNames = getColumnNames();
        final int columnNamesCount = columnNames.length;
        for (Map<String, Object> profile : profiles) {
            final ArrayList<String> row = new ArrayList<String>(columnNamesCount);

            for (int i = 0; i < columnNamesCount; i++) {
                row.add((String) profile.get(columnNames[i]));
            }
            addRow(row);
        }
    }
}
