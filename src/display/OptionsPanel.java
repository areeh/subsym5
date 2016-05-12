package display;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.Simulation;

public class OptionsPanel extends JPanel implements Observer {
	private Simulation sim;
	private JSpinner crossoverRate;
	private JSpinner mutateRate;
	private JSpinner genotypeSize;
	private JSpinner nrOfGenerations;
	private JSpinner generationSize;
	private JSpinner childrenSize;
	private JSpinner K;
	private JSpinner eChance;
	private JButton startButton;
	private JCheckBox toSuccess;
	private JCheckBox elitism;
	private JSpinner zLength;
	SwingWorker<Void, Void> worker;
	private JSpinner seqLength;
	private JSpinner symbolNr;
	JComboBox<String> mateSel;
	JComboBox<String> adultSel;

	/**
	 * Creates new options panel.
	 * 
	 * @param simulation simulation
	 */
	public OptionsPanel(final Simulation simulation) {
		super();
		this.sim = simulation;
		this.setLayout(new GridLayout(9, 4));
		
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            			@Override
            			public Void doInBackground() {
            				if (simulation.isStopped()) {
            					simulation.start();
            					startButton.setText("Stop");
            				} else {
            					//simulation.stop();
            					startButton.setText("Start");
            				}
            				return null;
            			}
            			
            			@Override
            			protected void done() {
            				startButton.setText("Start");
            			}
            		};
            		startButton.setText("Working...");
            		worker.execute();
            	}
        });

		this.add(new JLabel("Start Simulation"));
		this.add(startButton);

		
		crossoverRate = new JSpinner(new SpinnerNumberModel(sim.getSettings().getCrossoverRate(), 0.0, 1000, 0.01));
		crossoverRate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setCrossoverRate(((SpinnerNumberModel)crossoverRate.getModel()).getNumber().doubleValue());
			}            
		});

		this.add(new JLabel("Crossover Rate"));
		this.add(crossoverRate);
		
		mutateRate = new JSpinner(new SpinnerNumberModel(sim.getSettings().getMutateRate(), 0.0, 1000, 0.01));
		mutateRate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setMutateRate(((SpinnerNumberModel)mutateRate.getModel()).getNumber().doubleValue());
			}            
		});

		this.add(new JLabel("Mutation Rate"));
		this.add(mutateRate);
		
		nrOfGenerations = new JSpinner(new SpinnerNumberModel(sim.getSettings().getNrOfGenerations(), 1, 5000, 1));
		nrOfGenerations.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setNrOfGenerations(((SpinnerNumberModel)nrOfGenerations.getModel()).getNumber().intValue());
			}            
		});

		this.add(new JLabel("Number of Generations"));
		this.add(nrOfGenerations);
		
		generationSize = new JSpinner(new SpinnerNumberModel(sim.getSettings().getGenerationSize(), 1, 1000, 1));
		generationSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setGenerationSize(((SpinnerNumberModel)generationSize.getModel()).getNumber().intValue());
			}            
		});

		this.add(new JLabel("Generation Size"));
		this.add(generationSize);
		
		K = new JSpinner(new SpinnerNumberModel(sim.getSettings().getK(), 1, 1000, 1));
		K.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setK(((SpinnerNumberModel)K.getModel()).getNumber().intValue());
			}            
		});

		this.add(new JLabel("Size of Tournament Groups"));
		this.add(K);
		
		eChance = new JSpinner(new SpinnerNumberModel(sim.getSettings().getEChance(), 0.0, 1000, 0.01));
		eChance.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sim.getSettings().setEChance(((SpinnerNumberModel)eChance.getModel()).getNumber().doubleValue());
			}            
		});

		this.add(new JLabel("Chance of slipping through tournament"));
		this.add(eChance);
}

/**
 * Updates the number of boids. This will be called when the number of boids
 * changes in the flock.
 */
public void update(Observable obs, Object object) {
}

/**
 * Resets the gui components. This must be called after a simulation is
 * loaded from a file.
 */

public void finished() {
	System.out.println("Called reset");
	startButton.setText("Start");
}

/**
 * Sets the simulation. This must be called after a simulation is
 * loaded from a file.
 * 
 * @param sim simulation
 */

/*
    public void setSim(Simulation sim) {
        this.sim = sim;
    }
 */

/**
 * Changes the pause button text to 'Resume'. This should be called whenever
 * the game is paused.
 */

}
