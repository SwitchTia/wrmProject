package wrm.progetto_wrm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "closed_task")
public class ClosedTask {
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", nullable = false)
    private int id;

    @Column (name = "ct_name")
    private String taskName;

    @Column (name = "ct_code", nullable = false, unique = true)
    private int taskCode;

    @Column (name = "ct_budget")
    private double taskBudget;

    @Column (name = "ct_profit")
    private double taskProfit;

    @Version
    @Column (name = "version")
    private Long version;

    /*//Relationship ClosedTask and ActiveTask
    @OneToOne
    @JoinColumn (name = "task_id")
    private ActiveTask at; 

    /*Relationship ClosedTask and task
    @OneToOne
    @JsonIgnore
    @JoinColumn (name = "task_code")
    private Task t; */

    //Reverse relationship ClosedTask and Users
    @ManyToOne
    @JsonIgnore
    @JoinColumn (name = "user_id")
    private Users u;


    

}
