package hsw.vkapiapp4.vk;

import com.google.api.client.util.Key;

import java.util.List;

public class ApiResponse {
    @Key("response")
    public List<VkProfile> profiles;
}

