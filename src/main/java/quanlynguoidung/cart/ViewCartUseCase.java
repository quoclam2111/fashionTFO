package quanlynguoidung.cart;

import java.util.ArrayList;
import java.util.List;
import repository.CartRepository;
import repository.DTO.CartDTO;

public class ViewCartUseCase implements ViewCartInputBoundary {
    private final CartRepository repository;
    private final ViewCartOutputBoundary presenter;

    public ViewCartUseCase(CartRepository repository, ViewCartOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCartInputData inputData) {
        List<CartDTO> dtoList = repository.findCartItemsByUserId(inputData.getUserId());
        List<ViewCartOutputData> outputList = new ArrayList<>();

        for (CartDTO dto : dtoList) {
            outputList.add(new ViewCartOutputData(
                    dto.getProductName(),
                    dto.getPrice(),
                    dto.getQuantity()
            ));
        }

        presenter.present(outputList);
    }
}
