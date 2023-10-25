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
@Table (name = "active_task")
public class ActiveTask {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", nullable = false)
    private int id;

    @Column (name = "at_name")
    private String taskName;

    @Column (name = "at_code", nullable = false, unique = true)
    private int taskCode;

    @Column (name = "at_budget")
    private double taskBudget;

    @Column (name = "at_profit")
    private double taskProfit;

    @Version
    @Column (name = "version")
    private Long version;

    /*//Reverse relationship between ActiveTask and ClosedTask
    @OneToOne
    @JsonIgnore
    @JoinColumn (name = "task_id")
    private ClosedTask ct; */

    //Reverse relationship ActiveTask and User
    @ManyToOne
    @JsonIgnore
    @JoinColumn (name = "user_id")
    private Users u;
    

    
}

