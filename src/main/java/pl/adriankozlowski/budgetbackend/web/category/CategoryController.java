package pl.adriankozlowski.budgetbackend.web.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.CreateNewCategory;
import pl.adriankozlowski.budgetbackend.application.service.CategoryService;
import pl.adriankozlowski.budgetbackend.domain.model.Category;
import pl.adriankozlowski.budgetbackend.web.account.NewAccountRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Page<Category> getAllCategories(Pageable pageable, HttpServletResponse response) {
        List<Category> handle = categoryService.getAllCategories();
        response.setHeader("x-total-count", String.valueOf(handle.size()));
        return new PageImpl<>(handle);
    }

    @PostMapping
    public Category createNewAccount(@RequestBody NewAccountRequest request) {
        Category category = categoryService.createNewCategory(CreateNewCategory.builder().name(request.getName()).build());
        return category;
    }

}
