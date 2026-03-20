package com.example.OrderingFood.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotNull(message = "Tổng hóa đơn không được để trống")
    @Min(value = 0, message = "Phải >=0")
    private double totalAmount;
    private int status;
    private Long  userId;
    private List<InputItem> orderItems;

    @Getter
    @Setter
    public static class InputItem {
        private Long foodId;
        private int quantity;
    }

}
