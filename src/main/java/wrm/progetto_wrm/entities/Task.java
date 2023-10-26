package wrm.progetto_wrm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "task")
public class Task {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id", nullable = false)
    private int id;

    @Column (name = "task_name")
    private String taskName;

    @Column (name = "task_code", nullable = false, unique = true)
    private int taskCode;

    @Column (name = "task_cost")
    private double taskCost;

    @Column (name = "task_profit")
    private double taskProfit;

    @Version
    @Column (name = "version")
    private Long version;

    
    /*//Relationship Task and ClosedTask
    @OneToOne
    @JoinColumn (name = "task_id")
    private ClosedTask ct; */

    
}
