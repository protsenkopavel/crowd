package net.protsenko.crowd.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import net.protsenko.crowd.components.EmployeeEditor;
import net.protsenko.crowd.domain.Employee;
import net.protsenko.crowd.repo.EmployeeRepo;

@Route
public class MainView extends VerticalLayout {

    private final EmployeeRepo employeeRepo;

    private Grid<Employee> grid = new Grid<>(Employee.class);

    private final TextField filter = new TextField("" , "Type to filter");
    private final Button addNewBtn = new Button("Add new employee");
    private final HorizontalLayout toolBar = new HorizontalLayout(filter, addNewBtn);
    private final EmployeeEditor employeeEditor;

    public MainView(EmployeeRepo employeeRepo, EmployeeEditor employeeEditor) {
        this.employeeRepo = employeeRepo;
        this.employeeEditor = employeeEditor;

        add(toolBar, grid, employeeEditor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showEmployee(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            employeeEditor.editEmployee(e.getValue());
        });

        addNewBtn.addClickListener(e -> employeeEditor.editEmployee(new Employee()));

        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            showEmployee(filter.getValue());
        });

        showEmployee("");
    }

    private void showEmployee(String name) {
        if (name.isEmpty()) {
            grid.setItems(employeeRepo.findAll());
        } else {
            grid.setItems(employeeRepo.findByName(name));
        }
    }
}
