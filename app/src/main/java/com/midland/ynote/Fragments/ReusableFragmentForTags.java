package com.midland.ynote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.midland.ynote.Adapters.MotionPicturesSubCatRV;
import com.midland.ynote.R;

import java.util.ArrayList;
import java.util.Collections;

public class ReusableFragmentForTags extends Fragment {

    String subCategory;
    RecyclerView reusableTagsRV;
    public ArrayList<String> compSciTags, mathTags, statisticsTags, bioTags, chemTags, phycTags, earthSciTags, spaceSciTags;
    public ArrayList<String> performingArtsTags, visualArtsTags, historyTags, homeEconTags, languagesTags, lawTags, philosophyTags, theologyTags;
    public ArrayList<String> anthropologyTags, archaeologyTags, economicsTags, geographyTags, politicalTags, psychologyTags, sociologyTags, socialWorkTags;
    public ArrayList<String> businessTags, engineeringTags, medicineTags;


    public ReusableFragmentForTags(String subCategory) {
        this.subCategory = subCategory;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reusable_fragment_for_tags, container, false);
        reusableTagsRV = v.findViewById(R.id.reusableTagsRV);
        StaggeredGridLayoutManager stag = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        reusableTagsRV.setLayoutManager(stag);

        compSciTags = new ArrayList<>();
        mathTags = new ArrayList<>();
        statisticsTags = new ArrayList<>();
        bioTags = new ArrayList<>();
        chemTags = new ArrayList<>();
        phycTags = new ArrayList<>();
        earthSciTags = new ArrayList<>();
        spaceSciTags = new ArrayList<>();
        anthropologyTags = new ArrayList<>();
        archaeologyTags = new ArrayList<>();
        economicsTags = new ArrayList<>();
        geographyTags = new ArrayList<>();
        politicalTags = new ArrayList<>();
        psychologyTags = new ArrayList<>();
        sociologyTags = new ArrayList<>();
        socialWorkTags = new ArrayList<>();
        performingArtsTags = new ArrayList<>();
        visualArtsTags = new ArrayList<>();
        historyTags = new ArrayList<>();
        homeEconTags = new ArrayList<>();
        languagesTags = new ArrayList<>();
        lawTags = new ArrayList<>();
        philosophyTags = new ArrayList<>();
        theologyTags = new ArrayList<>();
        businessTags = new ArrayList<>();
        engineeringTags = new ArrayList<>();
        medicineTags = new ArrayList<>();

        Collections.addAll(compSciTags, "Loging in computer science", "Formal methods(formal verification)", "Logic programming", "Multi-valued logic", "Fuzzy logic", "Programming language semantics", "Type theory", "Algorithms", "Computational geometry", "Distirbuted algorithms", "Parallel algorithms", "Randomized algorithms", "Artificial intelligence", "Cognitive science", "Automated reasoning", "Computer vision",
                "Machine learning", "Artificial neural network", "Supportive vector machine", "Natural language processing(computational linguistics)", "Expert systems", "Robotics", "Data structures", "Computer architecture", "Computer graphics", "Image processing", "Scientific visualization", "Computer communications", "Cloud computing", "Information theory", "Internet, World Wide Web", "Ubiquitous computing", "Wireless computing(Mobile computing)", "Computer security & reliability",
                "Cryptography", "Fault-tolerant computing", "Computing in math, neural science, engineering & medicine", "Algebraic(Symbolic) computation", "Computational biology(Bioinformatics)", "Computational chemistry", "Computational mathematics", "Computational neuroscience", "Computational number theory", "Computational number theory", "Computational physics", "Computer aided engineering", "Computational fluid dynamics", "Finite element analysis", "Numerical analysis", "Scientific computing",
                "Computing in social sciences, arts & humanities", "Community informatics", "Computational economics", "Computational finance", "Computational sociology", "Digital humanities", "History of computer hardware", "History of computer science", "Humanistic informatics", "Database", "Distributed database", "Object database", "Relational database", "Data management", "Data mining", "Information architecture", "Information management", "Information retrieval", "Knowledge management", "Multimedia",
                "Hypermedia", "Sound & music computing", "Distributed computing", "Grid computing", "Human-computer interaction", "Operating systems", "Parallel computing", "High-performance computing", "Programming languages", "Compilers", "Programming paradigms", "Concurrent programming", "Functional programming", "Imperative programming", "Logic programming", "Objec oriented programming", "Program semantics", "Type theory", "Quantum computing", "Software engineering", "Theory of computation", "Automata theory(Formal languages)",
                "Comutability theory", "Computational complexity theory", "Concurrency theory", "VLSI design");

        Collections.addAll(mathTags, "Pure mathematics", "Mathematical logig", "Foundations of mathematics", "Intuitionistic logic", "Modal logic", "Model theory", "Proof theory", "Recursion theory", "Set theory", "Algebra", "Associative algebra", "Category theory", "Topos theory", "Differential algebra", "Field theory", "Group theory", "Group representation", "Homological algebra", "K-theory", "Lattice(Order) theory", "Lie algebra", "Linear algebra", "Linear algebra(Vector space)",
                "Multi-linear algebra", "Non-associative algebra", "Representation theory", "Ring theory", "Commutative algebra", "Non-commutative algebra", "Universal algebra", "Analysis", "Complex analysis", "Functional analysis", "Operation theory", "Harmonic analysis", "Fourier analysis", "Non-standard analysis", "Ordinary differential equations", "P-acid analysis", "Partial differential equations", "Real analysis", "Calculus", "Probability theory", "Ergodic theory", "Measure theory", "Integral geometry", "Stochastic process",
                "Geometry & topology", "Affine geometry", "Algebraic geometry", "Algebraic topology", "Convex geometry", "Differential topology", "Discrete geometry", "Finite geometry", "Galois geometry", "General topology", "Geometric topology", "Integral geometry", "Non-commutative geometry", "Non-Educlidean geometry", "Projective geometry", "Number theory", "Algebraic number theory", "Arithmetic combinatorics", "Geometric number theory", "Applied mathematics", "Approximation theory", "Combinatorics", "Coding theory", "Cryptography", "Dynamical systems",
                "Dynamical systems", "Chaos theory", "Fractal geometry", "Game theory", "Graph theory", "Information theory", "Mathematical physics", "Quantum field theory", "Quantum gravity", "String theory", "Quantam mechanics", "Statistical mechanics", "Numerical analysis", "Operations research", "Assignment problems", "Decision analysis", "Dynamic programming", "Inventory theory", "Linear programming", "Mathematical optimization", "Optimal maintenance", "Real options analysis", "Scheduling", "Stochastic processes", "Systems analysis", "Statistics", "Actuarial science",
                "Demography", "Econometrics", "Mathematical statistics", "Data visualization", "Theory of computation", "Computational complexity theory");

        Collections.addAll(statisticsTags, "Astro statistics", "Bio statistics");

        Collections.addAll(bioTags, "Aerobiology", "Anatomy", "Comparative anatomy", "Human anatomy", "Biochemistry", "Bioinformatics", "Biophysics", "Biotechnology", "Botany", "Ethnobotany", "Phycology", "Cell biology", "Chronobiology", "Computational biology", "Cryobiology", "Development biology", "Embryology", "Teratology", "Ecology", "Agro-ecology", "Ethno-ecology", "Human ecology", "Landscape ecology",
                "Endocrinology", "Evolutionary biology", "Genetics", "Behavioural genetics", "Molecular genetics", "Population genetics", "Histology", "Human biology", "Immunology", "Limnolgoy", "Linnaean taxonomy", "Marine biology", "Mathematical biology", "Micro-biology", "Bacteriology", "Protistology", "Molecular biology", "Mycology", "Neuroscience", "Behavioural neuroscience", "Nutrition", "Paleobiology", "Paleontology", "Parasitology", "Pathology", "Anatomical pathology",
                "Clinical pathology", "Dermato-pathology", "Forensic-pathology", "Hemato-pathology", "Histo-pathology", "Molecular pathology", "Surgical pathology", "Physiology", "Human physiology", "Exercise physiology", "Structural biology", "Systematics", "Systems biology", "Virology", "Molecular virology", "Xeno-biology", "Zoology", "Animal communications", "Apiology", "Arachnology", "Cracinology", "Cetology", "Entomology", "Forensic entomology", "Ethnozoology", "Ethology",
                "Helminthology", "Herpetology", "Ichthyology", "Mammalogy", "Malacology", "Conchology", "Myrmecology", "Nematology", "Neuroethology", "Oology", "Ornithology", "Planktology", "Primatology", "Zootomy", "Zoosemiotics");

        Collections.addAll(chemTags, "Agro-chemistry", "Analytical chemistry", "Astro-chemistry", "Atmospheric chemistry", "Biochemistry", "Chemical biology", "Chemical engineering", "Cheminformatics", "Computational chemistry", "Cosmochemistry", "Electro-chemistry", "Environmental chemistry", "Femto=chemistry", "Flavor", "Flow chemistry", "Geochemistry", "Green chemistry", "Histo-chemistry", "Hydrogenation", "Immunochemistry",
                "Inorganic chemistry", "Marine chemistry", "Mathematical chemistry", "Mechanochemistry", "Medicinal chemistry", "Molecular chemistry", "Molecular mechanics", "Nanotechnology", "Natural product chemistry", "Neurochemistry", "Oenology", "Organic chemistry", "Organometallic chemistry", "Petro-chemistry", "Pharmacology", "Photochemistry", "Physical chemistry", "Phyto-chemistry", "Polymer chemistry", "Quantum chemistry", "Radio-chemistry", "Solid state chemistry",
                "Sono-chemistry", "Supramolecualr chemistry", "Surface chemistry", "Synthetic chemistry", "Theoretical chemistry", "Thermo-chemistry");

        Collections.addAll(phycTags, "Acoustics", "Aerodynamics", "Applied physics", "Astro-physics", "Atomic, molecular & optical physics", "Biophysics", "Computational physics", "Condensed matter physics", "Cryogenics", "Electricity", "Electro-magnetism", "Elementary particle physics", "Experimental physics", "Fluid mechanics", "Geo-physics", "Mathematical physics", "Mechanics", "Medical physics", "Molecular physics", "Newtonian physics", "Nuclear physics", "Optics",
                "Plasma physics", "Quantum physics", "Solid mechanics", "Solid state physics", "Statistical mechanics", "Theoretical physics", "Thermal physics", "Thermodynamics");

        Collections.addAll(earthSciTags, "Edaphology", "Environmental chemistry", "Environmental science", "Gemology", "Geo-chemistry", "Geodesy", "Physical geography", "Atmospheric science(Meteorology)", "Biogeography(Phytogeography)", "Climatology(Paleoclimatology)", "Coastal geography(Oceanography)", "Edaphology/Pedology(Soil science)", "Geobiology", "Geology", "Mineralogy", "Petrology", "Sedimentology", "Speleology", "Tectonics", "Volcanology",
                "Geostatistics", "Hydrology/Limnology", "Landscape ecology", "Quaternary science", "Geophysics", "Paleontology", "Paleobiology", "Paleoecology");

        Collections.addAll(spaceSciTags, "Astro-biology", "Astronomy", "Observational astronomy", "Gamma ray astronomy", "Infrared astronomy", "Microwave astronomy", "Optical astronomy", "Radio astronomy", "UV astronomy", "X-ray astronomy", "Astrophysics", "Gravitational astronomy", "Black holes", "Interstellar medium", "Numerical simulations", "Astrophysical plasma", "Galaxy formation & evolution", "High-energy astro-physics", "Hydro-dynamics", "Magneto-hydrodynamcs",
                "Star formation", "Physical cosmology", "Stellar astrophysics", "Helio-seismology", "Stellar evolution", "Stellar nucleosynthesis", "Planetary science");

        Collections.addAll(performingArtsTags, "Music", "Accompanying", "Chamber music", "Church music",
                "Conducting", "Choral conducting", "Orchestral conducting", "Wind ensemble conducting", "Early music", "Jazz studies", "Musical composition",
                "Music education", "Music history", "Musicology", "Historical musicology", "Systematic musicology", "Entomology", "Music theory", "Orchestral studies",
                "Organology", "Organ & historical keyboards", "Piano", "Strings, harp, oud & guitar", "Singing", "Woodwinds, brass & percussion", "Recording",
                "Dance", "Choreography", "Dance notion", "Ethnochoreology", "History of dance", "Television", "Television studies", "Theatre", "Acting", "Directing",
                "Dramaturgy", "History", "Musical theatre", "Playwright", "Puppetry", "Scenography", "Stage design", "Ventriloquism", "Film", "Animation", "Film criticism,",
                "Film theory", "Live action");

        Collections.addAll(visualArtsTags, "Fine arts", "Graphic arts", "Drawing", "Painting", "Photography", "Sculpture", "Applied arts",
                "Animation", "Calligraphy", "Decorative arts", "Mixed media", "Printmaking", "Studio art", "Architecture", "Interior architecture", "Landscape architecture", "Landscape design",
                "Landscape planning", "Architectural analytics", "Historic preservation", "Interior design", "Technical drawing", "Fashion");

        Collections.addAll(historyTags, "African history", "American history", "Ancient history", "Ancient Egypt", "Carthage", "Ancient Greek history",
                "Ancient Roman history", "Assyrian civilization", "Bronze Age civilization", "Biblical history", "History of the Indus Valley civilization", "Pre Classic Maya", "History of Mesopotamia",
                "The Stone Age", "History of the Yangtze civilization", "History of the Yellow River civilization", "Asian history", "Chinese history", "Indian history", "Indonesian history", "Iranian history",
                "Australian history", "Ecclesiastical history of the Catholic Church", "Economic history", "Environmental history", "European history", "Intellectual history", "Jewish history", "Latin American history",
                "Modern history", "History of philosophy", "Ancient philosophy", "Contemporary philosophy", "Medieval philosophy", "Humanism", "Scholasticism", "Modern philosophy",  "Political history",
                "Pre Columbian era", "Russian history", "History of culture", "Scientific history", "Technological history", "World history", "Public history");

        Collections.addAll(homeEconTags, "Cooking", "Cleaning", "Clothing", "Family studies", "Finance", "Gardening", "Health", "Nutrition");

        Collections.addAll(languagesTags, "Linguistics", "Applied linguistics", "Composition studies", "Computational linguistics", "Discourse analysis", "English studies",
                "Etymology", "Grammar", "Grammatology", "Historical linguistics", "Interlinguistics", "Lexicology", "Linguistic typology", "Morphology", "Natural language processing", "Philology", "Phonetics", "Phonology",
                "Pragmatics", "Psycholinguistics", "Rhetoric", "Semantics", "Semiotics", "Sociolinguistics", "Syntax", "Usage", "Word usage", "Comparative literature", "Creative writing", "Fiction writing", "Non fiction writing",
                "English literature", "History of literature", "Medieval literature", "Post-colonial literature", "Post-modern literature", "Literary criticism", "Poetics", "Poetry", "World literature", "African-American literature",
                "American literature", "British literature");

        Collections.addAll(lawTags, "Administrative law", "Canon law", "Civil law", "Admiralty law", "Animal law", "Civil procedure", "Common law", "Contract law", "Corporations law", "Environmental law",
                "Family law", "Federal law", "International law", "Public international law", "Supranational law", "Labor law", "Property law", "Tax law", "Tort law", "Comparative law", "Competition law", "Constitutional law", "Criminal law", "Criminal justice",
                "Criminal procedure", "Forensic science", "Police science", "Islamic law", "Jewish law", "Jurisprudence(Philosophy of Law)", "Legal management", "Commercial law", "Corporate law", "Procedural law", "Substantive law");

        Collections.addAll(philosophyTags, "Aesthetics", "Applied philosophy", "Philosophy of economics", "Philosophy of education", "Philosophy of engineering", "Philosophy of history", "Philosophy of language",
                "Philosophy of law", "Philosophy of mathematics", "Philosophy of music", "Philosophy of psychology", "Philosophy of religion", "Philosophy of biology", "Philosophy of chemistry", "Philosophy of physics", "Philosophy of social science", "Philosophy of technolgoy",
                "Systems of philosophy", "Epistemology", "Justification", "Reasoning errors", "Ethics", "Applied ethics", "Animal rights", "Bioethics", "Environmental ethics", "Meta-ethics", "Moral psychology","Descriptive ethics", "Value theory", "Normative ethics", "Virtue ethics",
                "Virtue ethics", "Logic", "Mathematical logic", "Philosophical logic", "Meta-philosophy", "Metaphysics", "Philosophy of action", "Determinism & free will", "Ontology", "Philosophy of mind", "Philosophy of pain", "Philosophy of Artificial Intelligence", "Philosophy of percection",
                "Teleology", "Theism & Atheism", "Philosophy of traditions & schools", "African philosophy", "Analytic philosophy", "Aristotelianism", "Continental philosophy", "Eastern philosophy", "Feminist philosophy", "Platonism", "Social philosophy", "Political philosophy", "Anarchism",
                "Libertarianism", "Marxism");

        Collections.addAll(theologyTags, "Biblical studies", "Religious studies", "Biblical Hebrew", "Biblical Greek", "Aramaic", "Buddhist theology", "Christian theology", "Anglican theology", "Baptist theology", "Catholic theology", "Eastern Orthodox theology",
                "Protestant theology", "Hindu theology", "Jewish theology", "Muslim theology");

        Collections.addAll(anthropologyTags, "Biological anthropology", "Linguistic anthropology", "Cultural anthropology", "Social anthropology");

        Collections.addAll(archaeologyTags, "Archaeology");

        Collections.addAll(economicsTags, "Agricultural economics", "Anarchist economics", "Applied economics", "Behavioural economics", "Bio-economics", "Complexity economics", "Computational economics", "Consumer economics", "Development economics", "Ecological economics",
                "Econometrics", "Economic geography", "Economic sociology", "Economic systems", "Educational economics", "Energy economics", "Entrepreneurial economics", "Environmental economics", "Evolutionary economics", "Experimental economics", "Feminist economics", "Financial econometrics", "Financial economics",
                "Green economics", "Growth economics", "Human development theory", "Industrial organization", "Information economics", "Institutional economics", "International economics", "Islamic economics", "Labor economics", "Law & economics", "Macroeconomics", "Managerial economics", "Marxian economics", "Mathematics economics",
                "Microeconomics", "Monetary economics", "Neuroeconomics", "Participatory economics", "Political economy", "Public economics", "Public finance", "Real estate economics", "Resource economics", "Social choice theory", "Socialist economics", "Socioeconomics", "Transport economics", "Welfare economics");

        Collections.addAll(geographyTags, "Physical geography", "Atmology", "Biogeography", "Climatology","Coastal geography", "Emergency management", "Environmental geography", "Geobiology", "Geochemistry", "Geology", "Geomatics", "Geomorphology", "Geophysics", "Glaciology", "Hydrology",
                "Landscape ecology", "Lithology", "Meteorology", "Mireralogy", "Oceanography", "Palaeogeography", "Petrology", "Quaternary science", "Soil geography", "Human geography", "Behavioural geography", "Cognitive geography", "Cultural geography", "Development geography", "Economic geography", "Health geography",
                "Historical geography", "Language geography", "Marketing geography", "Military geography", "Political geography", "Population geography", "Religion geography", "Social geography", "Strategic geography", "Time geography", "Tourism geography", "Transport geography", "Urban geography", "Integrated geography",
                "Cartography", "Celestial cartography", "Planetary cartography", "Topography");

        Collections.addAll(politicalTags, "American poitics", "Canadian politics", "Civics", "Comparative politics", "European studies", "Geopolitics", "International relations", "International organizations", "Nationalism studies", "Peace & conflict studies", "Policy studies", "Political behaviour",
                "Political culture", "Political economy", "Political history", "Political philosophy", "Public administration", "Public law", "Psephology", "Social choice theory");

        Collections.addAll(psychologyTags, "Abnormal psychology", "Applied psychology", "Biological psychology", "Clinical neuropsychology", "Clinical psychology", "Cognitive psychology", "Community psychology", "Comparative psychology", "Conservation psychology", "Consumer psychology", "Counselling psychology",
                "Criminal psychology", "Cultural psychology", "Asian psychology", "Black psychology", "Developmental psychology", "Differential psychology", "Ecological psychology", "Educational psychology", "Evolutionary psychology", "Experimental psychology", "Group psychology", "Family psychology", "Feminine psychology", "Forensic developmental psychology",
                "Forensic psychology", "Health psychology", "Humanistic psychology", "Indigenous psychology", "Legal psychology", "Mathematical psychology", "Media psychology", "Medical psychology", "Military psychology", "Moral psychology & Descriptive ethics", "Music psychology", "Neuropsychology", "Occupational psychology", "Organizational psychology",
                "Parapsychology", "Parapsychology", "Pediatric psychology", "Pedology", "Personality psychology", "Phenomenology", "Political psychology", "Positive psychology", "Psychoanalysis", "Psychology & religion", "Psychometrics", "Psychopathology", "Child psychopathology", "Psychophysics", "Quantitative psychology", "Rehabilitation psychology",
                "School psychology", "Social psychology", "Sport psychology", "Traffic psychology", "Transpersonal psychology");

        Collections.addAll(sociologyTags, "Analytical sociology", "Applied sociology", "Leisure studies", "Political sociology", "Public sociology","Social engineering", "Architectural sociology", "Area studies", "African studies", "American studies", "Appalachian studies", "Canadian studies", "Latin American studies",
                "Asian studies", "Central Asian studies", "East Asian studies", "Indology", "Iranian studies", "Japanese studies", "Korean studies", "Pakistan studies", "Sindhology", "Sinology", "Southeast Asian studies", "Thai studies", "Australian studies", "European studies", "Celtic studies", "German studies", "Sociology in Poland", "Scandinavian studies",
                "Slavic studies","Middle Eastern studies", "Arab studies", "Assyriology", "Egyptology", "Jewish studies", "Behavioural studies", "Collective behaviour", "Social movements", "Community informatics", "Social network analysis", "Comparative sociology", "Conflict theory", "Criminology/Criminal justice", "Critical management studies", "Critical sociology",
                "Critical management studies", "Critical sociology", "Cultural sociology", "Cultural studies", "Africana studies", "Cross-cultural studies", "Culturology", "Deaf studies", "Ethnology", "Utopian studies", "Whiteness studies", "Demograhpy/Population", "Digital sociology", "Dramaturgical sociology","Economic sociology", "Educational sociology", "Empirical sociology",
                "Environmental sociology", "Evolutionary sociology", "Feminist sociology", "Figurational sociology", "Futures studies", "Gender studies", "Men's studies", "Women's studies", "Historical sociology", "Human ecology", "Humanistic sociology", "Industrial sociology", "Interactionism", "Imperative sociology", "Ethnomethodology", "Phenomenology", "Social constructionism",
                "Symbolic constructionism", "Jealousy sociology", "Macrosociology", "Marxist sociology", "Mathematical sociology", "Medical sociology", "Mesosociology", "Microsociology", "Military sociology", "Natural resource sociology", "Organizational studies", "Phenomenological sociology", "Policy sociology", "Psychoanalytic sociology", "Science studies", "Technology studies", "Sexology",
                "Hererosexism", "Human sexual behaviour", "Human sexuality", "Queer studies", "Queer theory", "Sex education", "Social capital", "Social change", "Social conflict theory", "Social control", "Pure sociology", "Social economy", "Social philosophy", "Social policy", "Social psychology", "Social stratification", "Social theory", "Social transformation", "Computational sociology", "Economic sociology",
                "Economic development", "Social development", "Sociobiology", "Sociocybernetics", "Sociolinguistics", "Sociology of aging", "Sociology of agriculture", "Sociology of art", "Sociology of autism", "Sociology of childhood", "Sociology of conflict", "Sociology of culture", "Sociology of cyberspace", "Sociology of development", "Sociology of deviance", "Sociology of disaster", "Sociology of education",
                "Sociology of education", "Sociology of emotions", "Sociology of fatherhood", "Sociology of finance", "Sociology of food", "Sociology of gender", "Sociology of generations", "Sociology of globalization", "Sociology of government", "Sociology of health & illness", "Sociology of human consciousness", "Sociology of immigration", "Sociology of knowledge", "Sociology of languages", "Sociology of law", "Sociology of leisure",
                "Sociology of literature", "Sociology of markets", "Sociology of motherhood", "Sociology of music", "Sociology of Natural resources", "Sociology of organization", "Sociology of peace, war & social conflict", "Sociology of punishment", "Sociology of race & ethic relations", "Sociology of religion", "Sociology of risk", "Sociology of science", "Sociology of scientific knowledge", "Sociology of social change", "Sociology of social movement",
                "Sociology of space", "Sociology of sport", "Sociology of technology", "Sociology of terrorism", "Sociology of the body", "Sociology of the family", "Sociology of the history of science", "Sociology of the internet", "Sociology of work", "Sociomusicology", "Structural sociology", "Theoretical sociology", "Urban sociology", "Rural sociology", "Victimology", "Visual sociology");

        Collections.addAll(socialWorkTags, "Clinical social word", "Community practice", "Mental health", "Psychosocial rehabilitation", "Person centered therapy", "Family therapy", "Financial social work");

        Collections.addAll(businessTags, "Accounting", "Business management", "Finance", "Marketing", "Operations management");

        Collections.addAll(engineeringTags, "Chemical engineering", "Bioengineering", "Biochemical engineering", "Bio-molecular engineering", "Catalysis", "Material engineering", "Molecular engineering", "Nanotechnology", "Polymer engineering", "Process design", "Petroleum engineering", "Nuclear engineering", "Food engineering", "Process engineering", "Reaction engineering", "Thermodynamics", "Transport phenomenon", "Civil engineering", "Coastal engineering", "Earthquake engineering", "Ecological engineering", "Environmental engineering",
                "Geo-technical engineering", "Engineering geology", "Hydraulic engineering", "Transportation engineering", "Highway engineering", "Structural engineering", "Architectural engineering", "Structure mechanics", "Surveying", "Educational technology", "Instructional design", "Distance education", "Instructional simulation", "Human performance technology", "Knowledge management", "Electrical engineering", "Applied physics", "Computer engineering", "Computer science", "Control systems engineering", "Control engineering", "Electronic engineering", "Instrumentation engineering",
                "Engineering physics", "Photonics", "Information theory", "Mechatronics", "Power engineering", "Quantum computing", "Roboics", "Semiconductors", "Telecommunication engineering", "Material science & engineering", "Biomaterials", "Ceramics engineering", "Crystallography", "Nano-materials", "Photonics", "Physical metallurgy", "Polymer engineering", "Polymer science", "Semiconductors", "Mechanical engineering", "Mechanical engineering", "Aeronautics", "Astronautics", "Acoustical engineering", "Automotive engineering", "Biomedical engineering", "Bio-mechanical engineering", "Neural engineering",
                "Continuum engineering", "Fluid mechnics", "Heat transfer", "Industrial engineering", "Manufacturing engineering", "Marine engineering", "Mass transfer", "Mechatronics", "Nano-engineering", "Ocean engineering", "Optical engineering", "Robotics", "Theromodynamics", "System science", "Chaos theory", "Complex systems", "Conceptual systems", "Control theory", "Affect control theory", "Control systems", "Control engineering", "Dynamic systems", "Perceptual control systems", "Cybermetics", "Bio-cybermetics", "Engineering cybermetics", "Management cybermetics", "Medical cybermetics", "New cybermetics",
                "Second order cybermetics", "Socio-cybermetics", "Network science", "Operation research", "Systems biology", "Computational systems biology","Synthetic  biology", "Systems immunology", "Systems neuroscience", "System dynamcis", "Social dynamics", "Systems ecology", "Ecosystem ecology", "Systems engineering", "Biological systems engineering", "Earth systems engineering & management", "Enterprise systems engineering", "Systems analysis", "Systems psychology", "Ergonomics", "Family systems theory", "Systemic therapy", "Systems theory", "Bio-mechanical systems theory", "Ecological systems theory", "Developmental systems theory",
                "General systems theory", "Living systems theory", "LTI systems theory", "Mathematical systems theory", "Socio-technology systems theory", "World systems theory", "Systems theory in anthropology");

        Collections.addAll(medicineTags, "");

        switch (subCategory) {
            case "Computer science":
                MotionPicturesSubCatRV adapter = new MotionPicturesSubCatRV(getContext(), compSciTags, "lectureTags");
                adapter.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter);

                break;

            case "Mathematics":
                MotionPicturesSubCatRV adapter1 = new MotionPicturesSubCatRV(getContext(), mathTags, "lectureTags");
                adapter1.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter1);
                break;

            case "Statistics":
                MotionPicturesSubCatRV adapter2 = new MotionPicturesSubCatRV(getContext(), statisticsTags, "lectureTags");
                adapter2.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter2);
                break;

            case "Biology":
                MotionPicturesSubCatRV adapter3 = new MotionPicturesSubCatRV(getContext(), bioTags, "lectureTags");
                adapter3.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter3);
                break;

            case "Chemistry":
                MotionPicturesSubCatRV adapter4 = new MotionPicturesSubCatRV(getContext(), chemTags, "lectureTags");
                adapter4.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter4);
                break;

            case "Earth science":
                MotionPicturesSubCatRV adapter5 = new MotionPicturesSubCatRV(getContext(), earthSciTags, "lectureTags");
                adapter5.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter5);
                break;

            case "Space science":
                MotionPicturesSubCatRV adapter6 = new MotionPicturesSubCatRV(getContext(), spaceSciTags, "lectureTags");
                adapter6.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter6);
                break;

            case "Physics":
                MotionPicturesSubCatRV adapter7 = new MotionPicturesSubCatRV(getContext(), phycTags, "lectureTags");
                adapter7.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter7);
                break;

            case "Performing arts":
                MotionPicturesSubCatRV adapter8 = new MotionPicturesSubCatRV(getContext(), performingArtsTags, "lectureTags");
                adapter8.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter8);
                break;

            case "Visual arts":
                MotionPicturesSubCatRV adapter9 = new MotionPicturesSubCatRV(getContext(), visualArtsTags, "lectureTags");
                adapter9.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter9);
                break;

            case "History":
                MotionPicturesSubCatRV adapter10 = new MotionPicturesSubCatRV(getContext(), historyTags, "lectureTags");
                adapter10.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter10);
                break;

            case "Home economics":
                MotionPicturesSubCatRV adapter11 = new MotionPicturesSubCatRV(getContext(), homeEconTags, "lectureTags");
                adapter11.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter11);
                break;

            case "Languages":
                MotionPicturesSubCatRV adapter12 = new MotionPicturesSubCatRV(getContext(), languagesTags, "lectureTags");
                adapter12.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter12);
                break;

            case "Law":
                MotionPicturesSubCatRV adapter13 = new MotionPicturesSubCatRV(getContext(), lawTags, "lectureTags");
                adapter13.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter13);
                break;

            case "Philosophy":
                MotionPicturesSubCatRV adapter14 = new MotionPicturesSubCatRV(getContext(), philosophyTags, "lectureTags");
                adapter14.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter14);
                break;

            case "Theology":
                MotionPicturesSubCatRV adapter15 = new MotionPicturesSubCatRV(getContext(), theologyTags, "lectureTags");
                adapter15.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter15);
                break;


            case "Anthropology":
                MotionPicturesSubCatRV adapter16 = new MotionPicturesSubCatRV(getContext(), anthropologyTags, "lectureTags");
                adapter16.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter16);
                break;

            case "Archaeology":
                MotionPicturesSubCatRV adapter17 = new MotionPicturesSubCatRV(getContext(), archaeologyTags, "lectureTags");
                adapter17.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter17);
                break;

            case "Economics":
                MotionPicturesSubCatRV adapter18 = new MotionPicturesSubCatRV(getContext(), economicsTags, "lectureTags");
                adapter18.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter18);
                break;

            case "Geography":
                MotionPicturesSubCatRV adapter19 = new MotionPicturesSubCatRV(getContext(), geographyTags, "lectureTags");
                adapter19.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter19);
                break;

            case "Political science":
                MotionPicturesSubCatRV adapter20 = new MotionPicturesSubCatRV(getContext(), politicalTags, "lectureTags");
                adapter20.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter20);
                break;

            case "Psychology":
                MotionPicturesSubCatRV adapter21 = new MotionPicturesSubCatRV(getContext(), psychologyTags, "lectureTags");
                adapter21.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter21);
                break;

            case "Sociology":
                MotionPicturesSubCatRV adapter22 = new MotionPicturesSubCatRV(getContext(), sociologyTags, "lectureTags");
                adapter22.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter22);
                break;

            case "Social work":
                MotionPicturesSubCatRV adapter23 = new MotionPicturesSubCatRV(getContext(), socialWorkTags, "lectureTags");
                adapter23.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter23);
                break;

            case "Business":
                MotionPicturesSubCatRV adapter24 = new MotionPicturesSubCatRV(getContext(), businessTags, "lectureTags");
                adapter24.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter24);
                break;

            case "Engineering":
                MotionPicturesSubCatRV adapter25 = new MotionPicturesSubCatRV(getContext(), engineeringTags, "lectureTags");
                adapter25.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter25);
                break;

            case "Medicine":
                MotionPicturesSubCatRV adapter26 = new MotionPicturesSubCatRV(getContext(), medicineTags, "lectureTags");
                adapter26.notifyDataSetChanged();
                reusableTagsRV.setAdapter(adapter26);
                break;


        }

        return v;
    }
}
