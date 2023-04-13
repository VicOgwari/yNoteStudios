package com.midland.ynote.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.midland.ynote.Objects.ImageObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class DocSorting {

    public static ArrayList<String> generalLectureCategories = new ArrayList<>();
    public static ArrayList<String> knowledgeBase = new ArrayList<>();
    public static ArrayList<String[]> subFields = new ArrayList<>();
    public static ArrayList<String[]> vidSubFields = new ArrayList<>();
    public static Map<String, String[]> docPlacerAndFinder = new HashMap<>();
    public static Map<String, String[]> lectureSubFieldTags = new HashMap<>();
    private static Map<String, String[]> vidPlacerAndFinder = new HashMap<>();


    public static String[] getSubCategories(String fieldName) {
        return docPlacerAndFinder.get(fieldName);
    }


    public static String getMainLectureField(int pos){

        generalLectureCategories.add("Humanities");
        generalLectureCategories.add("Social Sciences");
        generalLectureCategories.add("Natural Sciences");
        generalLectureCategories.add("Formal Sciences");
        generalLectureCategories.add("Applied Sciences");

        return generalLectureCategories.get(pos);
    }

    public static ArrayList<ImageObject> getFileObjects(Context con){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<ImageObject> imageObjects = new ArrayList<>();
        String[] directories = null;
        String orderBy = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            orderBy = MediaStore.Video.Media.DATE_TAKEN;
        }

        if (uri != null){
//            c = managedQuery(uri, projection, null, null, orderBy + " DESC");
            c = con.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        }

        if ((c != null) && (c.moveToFirst())){
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
        }

        for (int i = 0; i < dirList.size(); i++){
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();

            if (imageList != null){
                for (File imagePath : imageList){
                    try {
                        if (imagePath.isDirectory()){
                            imageList = imagePath.listFiles();
                        }
                        if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG") ||
                                imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG") ||
                                imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG") ||
                                imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF") ||
                                imagePath.getName().contains(".bpm") || imagePath.getName().contains(".BPM")){

                            File imageFile = imagePath.getAbsoluteFile();
                            long dateModified = imageFile.lastModified();
                            ImageObject imageObject = new ImageObject(imageFile.getName(), Uri.fromFile(imageFile), dateModified);
                            imageObjects.add(imageObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Toast.makeText(con, "Gallery empty!", Toast.LENGTH_SHORT).show();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageObjects.sort(Comparator.comparing(imageObject -> imageObject.getDateModified()));
        }
        Collections.reverse(imageObjects);
        return imageObjects;
    }


    public static String[] getSubFields(int position) {
        subFields.add(new String[]{"Agribusiness management & trade", "Agricultural resource management", "Agricultural science & technology"});
        subFields.add(new String[]{"Community resource management & extension", "Fashion design & marketing", "Food, nutrition & dietetics", "Recreation management & exercise science"
                , "Physical and health education"});
        subFields.add(new String[]{"Management science", "Accounting & finance", "Business administration"});
        subFields.add(new String[]{"Applied economics", "Econometrics & statistics", "Economic theory"});
        subFields.add(new String[]{"Educational foundations", "Early childhood studies", "Educational communication & technology", "Education management, policy & curriculum studies"
                , "Educational psychology", "Library & information science", "Special needs education"});
        subFields.add(new String[]{"Civil engineering", "Electrical & electronics engineering", "Mechanical engineering", "Energy technology", "Gas & petroleum engineering", "Computing & information technology"});
        subFields.add(new String[]{"Environmental education", "Environmental planning & development", "Environmental science", "Environmental studies & community development"});
        subFields.add(new String[]{"Hospitality management", "Tourism management"});
        subFields.add(new String[]{"English & linguistics", "Foreign Languages", "Literature", "Psychology", "Sociology", "Philosophy & religious studies", "History, archaeology & political studies"
                , "Gender & development studies", "Geography", "Swahili & African languages", "Public policy & administration"});
        subFields.add(new String[]{"Private law", "Public law", "Criminal law", "Constitutional law", "Civil law", "Administrative law"});
        subFields.add(new String[]{"Human anatomy", "Medical physiology", "Medicine, therapeutics, dermatology & psychiatry", "Obstetrics & gynecology", "Paediatrics & child health", "Pathology"
                , "Surgery & orthopaedics", "Nursing sciences", "Pharmacy & complementary(Alternative medicine)", "Medical laboratory science"});
        subFields.add(new String[]{"Health management & informatics", "Environmental & occupational health", "Community health", "Mental health"});
        subFields.add(new String[]{"Biochemistry & biotechnology", "Chemistry", "Physics", "Microbiology", "Mathematics", "Statistics & actuarial science", "Plant science(Botany)", "Zoology science"});
        subFields.add(new String[]{"Music & dance", "Fine art & design", "Theatre arts & film technology"});
        subFields.add(new String[]{});
        subFields.add(new String[]{});
        subFields.add(new String[]{"Film & theatre arts", "Communication & media studies"});
        subFields.add(new String[]{"Architecture & interior design", "Construction & real estate management", "Urban & environmental planning"});
        subFields.add(new String[]{});

        return subFields.get(position);
    }

    public static String[] getLectureSubFields(int position) {


        //HUMANITIES
        vidSubFields.add(new String[]{"Performing arts", "Visual arts", "History", "Home economics", "Languages & literature", "Law", "Philosophy", "Theology"});

        //SOCIAL SCIENCES
        vidSubFields.add(new String[]{"Anthropology", "Archaeology", "Economics", "Geography", "Political science", "Psychology", "Sociology", "Social work"});

        //NATURAL SCIENCES
        vidSubFields.add(new String[]{"Biology", "Chemistry", "Earth science", "Space science", "Physics"});

        //FORMAL SCIENCES
        vidSubFields.add(new String[]{"Computer science", "Mathematics", "Statistics"});

        //APPLIED SCIENCES
        vidSubFields.add(new String[]{"Business", "Engineering & technology", "Medicine & health"});

        return vidSubFields.get(position);
    }


    static {
        //HUMANITIES
        lectureSubFieldTags.put(getLectureSubFields(0)[0], new String[]{"Music", "Accompanying", "Chamber music", "Church music",
        "Conducting", "Choral conducting", "Orchestral conducting", "Wind ensemble conducting", "Early music", "Jazz studies", "Musical composition",
        "Music education", "Music history", "Musicology", "Historical musicology", "Systematic musicology", "Entomology", "Music theory", "Orchestral studies",
        "Organology", "Organ & historical keyboards", "Piano", "Strings, harp, oud & guitar", "Singing", "Woodwinds, brass & percussion", "Recording",
        "Dance", "Choreography", "Dance notion", "Ethnochoreology", "History of dance", "Television", "Television studies", "Theatre", "Acting", "Directing",
        "Dramaturgy", "History", "Musical theatre", "Playwright", "Puppetry", "Scenography", "Stage design", "Ventriloquism", "Film", "Animation", "Film criticism,",
        "Film theory", "Live action"});

        lectureSubFieldTags.put(getLectureSubFields(0)[1], new String[]{"Fine arts", "Graphic arts", "Drawing", "Painting", "Photography", "Sculpture", "Applied arts",
        "Animation", "Calligraphy", "Decorative arts", "Mixed media", "Printmaking", "Studio art", "Architecture", "Interior architecture", "Landscape architecture", "Landscape design",
        "Landscape planning", "Architectural analytics", "Historic preservation", "Interior design", "Technical drawing", "Fashion"});

        lectureSubFieldTags.put(getLectureSubFields(0)[2], new String[]{"African history", "American history", "Ancient history", "Ancient Egypt", "Carthage", "Ancient Greek history",
        "Ancient Roman history", "Assyrian civilization", "Bronze Age civilization", "Biblical history", "History of the Indus Valley civilization", "Pre Classic Maya", "History of Mesopotamia",
        "The Stone Age", "History of the Yangtze civilization", "History of the Yellow River civilization", "Asian history", "Chinese history", "Indian history", "Indonesian history", "Iranian history",
        "Australian history", "Ecclesiastical history of the Catholic Church", "Economic history", "Environmental history", "European history", "Intellectual history", "Jewish history", "Latin American history",
        "Modern history", "History of philosophy", "Ancient philosophy", "Contemporary philosophy", "Medieval philosophy", "Humanism", "Scholasticism", "Modern philosophy",  "Political history",
        "Pre Columbian era", "Russian history", "History of culture", "Scientific history", "Technological history", "World history", "Public history"});

        lectureSubFieldTags.put(getLectureSubFields(0)[3], new String[]{"Cooking", "Cleaning", "Clothing", "Family studies", "Finance", "Gardening", "Health", "Nutrition"});

        lectureSubFieldTags.put(getLectureSubFields(0)[4], new String[]{"Linguistics", "Applied linguistics", "Composition studies", "Computational linguistics", "Discourse analysis", "English studies",
        "Etymology", "Grammar", "Grammatology", "Historical linguistics", "Interlinguistics", "Lexicology", "Linguistic typology", "Morphology", "Natural language processing", "Philology", "Phonetics", "Phonology",
        "Pragmatics", "Psycholinguistics", "Rhetoric", "Semantics", "Semiotics", "Sociolinguistics", "Syntax", "Usage", "Word usage", "Comparative literature", "Creative writing", "Fiction writing", "Non fiction writing",
        "English literature", "History of literature", "Medieval literature", "Post-colonial literature", "Post-modern literature", "Literary criticism", "Poetics", "Poetry", "World literature", "African-American literature",
        "American literature", "British literature"});

        lectureSubFieldTags.put(getLectureSubFields(0)[5], new String[]{"Administrative law", "Canon law", "Civil law", "Admiralty law", "Animal law", "Civil procedure", "Common law", "Contract law", "Corporations law", "Environmental law",
        "Family law", "Federal law", "International law", "Public international law", "Supranational law", "Labor law", "Property law", "Tax law", "Tort law", "Comparative law", "Competition law", "Constitutional law", "Criminal law", "Criminal justice",
        "Criminal procedure", "Forensic science", "Police science", "Islamic law", "Jewish law", "Jurisprudence(Philosophy of Law)", "Legal management", "Commercial law", "Corporate law", "Procedural law", "Substantive law"});

        lectureSubFieldTags.put(getLectureSubFields(0)[6], new String[]{"Aesthetics", "Applied philosophy", "Philosophy of economics", "Philosophy of education", "Philosophy of engineering", "Philosophy of history", "Philosophy of language",
        "Philosophy of law", "Philosophy of mathematics", "Philosophy of music", "Philosophy of psychology", "Philosophy of religion", "Philosophy of biology", "Philosophy of chemistry", "Philosophy of physics", "Philosophy of social science", "Philosophy of technolgoy",
        "Systems of philosophy", "Epistemology", "Justification", "Reasoning errors", "Ethics", "Applied ethics", "Animal rights", "Bioethics", "Environmental ethics", "Meta-ethics", "Moral psychology","Descriptive ethics", "Value theory", "Normative ethics", "Virtue ethics",
        "Virtue ethics", "Logic", "Mathematical logic", "Philosophical logic", "Meta-philosophy", "Metaphysics", "Philosophy of action", "Determinism & free will", "Ontology", "Philosophy of mind", "Philosophy of pain", "Philosophy of Artificial Intelligence", "Philosophy of percection",
        "Teleology", "Theism & Atheism", "Philosophy of traditions & schools", "African philosophy", "Analytic philosophy", "Aristotelianism", "Continental philosophy", "Eastern philosophy", "Feminist philosophy", "Platonism", "Social philosophy", "Political philosophy", "Anarchism",
        "Libertarianism", "Marxism"});

        lectureSubFieldTags.put(getLectureSubFields(0)[7], new String[]{"Biblical studies", "Religious studies", "Biblical Hebrew", "Biblical Greek", "Aramaic", "Buddhist theology", "Christian theology", "Anglican theology", "Baptist theology", "Catholic theology", "Eastern Orthodox theology",
        "Protestant theology", "Hindu theology", "Jewish theology", "Muslim theology"});

        //SOCIAL SCIENCES
        //anthropology
        lectureSubFieldTags.put(getLectureSubFields(1)[0], new String[]{"Biological anthropology", "Linguistic anthropology", "Cultural anthropology", "Social anthropology"});
        //archaeology
        lectureSubFieldTags.put(getLectureSubFields(1)[1], new String[]{"Archaeology"});
        //economics
        lectureSubFieldTags.put(getLectureSubFields(1)[2], new String[]{"Agricultural economics", "Anarchist economics", "Applied economics", "Behavioural economics", "Bio-economics", "Complexity economics", "Computational economics", "Consumer economics", "Development economics", "Ecological economics",
                "Econometrics", "Economic geography", "Economic sociology", "Economic systems", "Educational economics", "Energy economics", "Entrepreneurial economics", "Environmental economics", "Evolutionary economics", "Experimental economics", "Feminist economics", "Financial econometrics", "Financial economics",
        "Green economics", "Growth economics", "Human development theory", "Industrial organization", "Information economics", "Institutional economics", "International economics", "Islamic economics", "Labor economics", "Law & economics", "Macroeconomics", "Managerial economics", "Marxian economics", "Mathematics economics",
        "Microeconomics", "Monetary economics", "Neuroeconomics", "Participatory economics", "Political economy", "Public economics", "Public finance", "Real estate economics", "Resource economics", "Social choice theory", "Socialist economics", "Socioeconomics", "Transport economics", "Welfare economics"});
        //geography
        lectureSubFieldTags.put(getLectureSubFields(1)[3], new String[]{"Physical geography", "Atmology", "Biogeography", "Climatology","Coastal geography", "Emergency management", "Environmental geography", "Geobiology", "Geochemistry", "Geology", "Geomatics", "Geomorphology", "Geophysics", "Glaciology", "Hydrology",
                "Landscape ecology", "Lithology", "Meteorology", "Mireralogy", "Oceanography", "Palaeogeography", "Petrology", "Quaternary science", "Soil geography", "Human geography", "Behavioural geography", "Cognitive geography", "Cultural geography", "Development geography", "Economic geography", "Health geography",
                "Historical geography", "Language geography", "Marketing geography", "Military geography", "Political geography", "Population geography", "Religion geography", "Social geography", "Strategic geography", "Time geography", "Tourism geography", "Transport geography", "Urban geography", "Integrated geography",
                "Cartography", "Celestial cartography", "Planetary cartography", "Topography"});
        //political science
        lectureSubFieldTags.put(getLectureSubFields(1)[4], new String[]{"American poitics", "Canadian politics", "Civics", "Comparative politics", "European studies", "Geopolitics", "International relations", "International organizations", "Nationalism studies", "Peace & conflict studies", "Policy studies", "Political behaviour",
                "Political culture", "Political economy", "Political history", "Political philosophy", "Public administration", "Public law", "Psephology", "Social choice theory"});
        //psychology
        lectureSubFieldTags.put(getLectureSubFields(1)[5], new String[]{"Abnormal psychology", "Applied psychology", "Biological psychology", "Clinical neuropsychology", "Clinical psychology", "Cognitive psychology", "Community psychology", "Comparative psychology", "Conservation psychology", "Consumer psychology", "Counselling psychology",
        "Criminal psychology", "Cultural psychology", "Asian psychology", "Black psychology", "Developmental psychology", "Differential psychology", "Ecological psychology", "Educational psychology", "Evolutionary psychology", "Experimental psychology", "Group psychology", "Family psychology", "Feminine psychology", "Forensic developmental psychology",
        "Forensic psychology", "Health psychology", "Humanistic psychology", "Indigenous psychology", "Legal psychology", "Mathematical psychology", "Media psychology", "Medical psychology", "Military psychology", "Moral psychology & Descriptive ethics", "Music psychology", "Neuropsychology", "Occupational psychology", "Organizational psychology",
                "Parapsychology", "Parapsychology", "Pediatric psychology", "Pedology", "Personality psychology", "Phenomenology", "Political psychology", "Positive psychology", "Psychoanalysis", "Psychology & religion", "Psychometrics", "Psychopathology", "Child psychopathology", "Psychophysics", "Quantitative psychology", "Rehabilitation psychology",
        "School psychology", "Social psychology", "Sport psychology", "Traffic psychology", "Transpersonal psychology"});
        //sociology
        lectureSubFieldTags.put(getLectureSubFields(1)[6], new String[]{"Analytical sociology", "Applied sociology", "Leisure studies", "Political sociology", "Public sociology","Social engineering", "Architectural sociology", "Area studies", "African studies", "American studies", "Appalachian studies", "Canadian studies", "Latin American studies",
                "Asian studies", "Central Asian studies", "East Asian studies", "Indology", "Iranian studies", "Japanese studies", "Korean studies", "Pakistan studies", "Sindhology", "Sinology", "Southeast Asian studies", "Thai studies", "Australian studies", "European studies", "Celtic studies", "German studies", "Sociology in Poland", "Scandinavian studies",
                "Slavic studies","Middle Eastern studies", "Arab studies", "Assyriology", "Egyptology", "Jewish studies", "Behavioural studies", "Collective behaviour", "Social movements", "Community informatics", "Social network analysis", "Comparative sociology", "Conflict theory", "Criminology/Criminal justice", "Critical management studies", "Critical sociology",
        "Critical management studies", "Critical sociology", "Cultural sociology", "Cultural studies", "Africana studies", "Cross-cultural studies", "Culturology", "Deaf studies", "Ethnology", "Utopian studies", "Whiteness studies", "Demograhpy/Population", "Digital sociology", "Dramaturgical sociology","Economic sociology", "Educational sociology", "Empirical sociology",
        "Environmental sociology", "Evolutionary sociology", "Feminist sociology", "Figurational sociology", "Futures studies", "Gender studies", "Men's studies", "Women's studies", "Historical sociology", "Human ecology", "Humanistic sociology", "Industrial sociology", "Interactionism", "Imperative sociology", "Ethnomethodology", "Phenomenology", "Social constructionism",
        "Symbolic constructionism", "Jealousy sociology", "Macrosociology", "Marxist sociology", "Mathematical sociology", "Medical sociology", "Mesosociology", "Microsociology", "Military sociology", "Natural resource sociology", "Organizational studies", "Phenomenological sociology", "Policy sociology", "Psychoanalytic sociology", "Science studies", "Technology studies", "Sexology",
        "Hererosexism", "Human sexual behaviour", "Human sexuality", "Queer studies", "Queer theory", "Sex education", "Social capital", "Social change", "Social conflict theory", "Social control", "Pure sociology", "Social economy", "Social philosophy", "Social policy", "Social psychology", "Social stratification", "Social theory", "Social transformation", "Computational sociology", "Economic sociology",
        "Economic development", "Social development", "Sociobiology", "Sociocybernetics", "Sociolinguistics", "Sociology of aging", "Sociology of agriculture", "Sociology of art", "Sociology of autism", "Sociology of childhood", "Sociology of conflict", "Sociology of culture", "Sociology of cyberspace", "Sociology of development", "Sociology of deviance", "Sociology of disaster", "Sociology of education",
        "Sociology of education", "Sociology of emotions", "Sociology of fatherhood", "Sociology of finance", "Sociology of food", "Sociology of gender", "Sociology of generations", "Sociology of globalization", "Sociology of government", "Sociology of health & illness", "Sociology of human consciousness", "Sociology of immigration", "Sociology of knowledge", "Sociology of languages", "Sociology of law", "Sociology of leisure",
        "Sociology of literature", "Sociology of markets", "Sociology of motherhood", "Sociology of music", "Sociology of Natural resources", "Sociology of organization", "Sociology of peace, war & social conflict", "Sociology of punishment", "Sociology of race & ethic relations", "Sociology of religion", "Sociology of risk", "Sociology of science", "Sociology of scientific knowledge", "Sociology of social change", "Sociology of social movement",
        "Sociology of space", "Sociology of sport", "Sociology of technology", "Sociology of terrorism", "Sociology of the body", "Sociology of the family", "Sociology of the history of science", "Sociology of the internet", "Sociology of work", "Sociomusicology", "Structural sociology", "Theoretical sociology", "Urban sociology", "Rural sociology", "Victimology", "Visual sociology"});
        //social work
        lectureSubFieldTags.put(getLectureSubFields(1)[7], new String[]{"Clinical social word", "Community practice", "Mental health", "Psychosocial rehabilitation", "Person centered therapy", "Family therapy", "Financial social work"});


        //NATURAL SCIENCE
        //biology
        lectureSubFieldTags.put(getLectureSubFields(2)[0], new String[]{"Aerobiology", "Anatomy", "Comparative anatomy", "Human anatomy", "Biochemistry", "Bioinformatics", "Biophysics", "Biotechnology", "Botany", "Ethnobotany", "Phycology", "Cell biology", "Chronobiology", "Computational biology", "Cryobiology", "Development biology", "Embryology", "Teratology", "Ecology", "Agro-ecology", "Ethno-ecology", "Human ecology", "Landscape ecology",
        "Endocrinology", "Evolutionary biology", "Genetics", "Behavioural genetics", "Molecular genetics", "Population genetics", "Histology", "Human biology", "Immunology", "Limnolgoy", "Linnaean taxonomy", "Marine biology", "Mathematical biology", "Micro-biology", "Bacteriology", "Protistology", "Molecular biology", "Mycology", "Neuroscience", "Behavioural neuroscience", "Nutrition", "Paleobiology", "Paleontology", "Parasitology", "Pathology", "Anatomical pathology",
        "Clinical pathology", "Dermato-pathology", "Forensic-pathology", "Hemato-pathology", "Histo-pathology", "Molecular pathology", "Surgical pathology", "Physiology", "Human physiology", "Exercise physiology", "Structural biology", "Systematics", "Systems biology", "Virology", "Molecular virology", "Xeno-biology", "Zoology", "Animal communications", "Apiology", "Arachnology", "Cracinology", "Cetology", "Entomology", "Forensic entomology", "Ethnozoology", "Ethology",
        "Helminthology", "Herpetology", "Ichthyology", "Mammalogy", "Malacology", "Conchology", "Myrmecology", "Nematology", "Neuroethology", "Oology", "Ornithology", "Planktology", "Primatology", "Zootomy", "Zoosemiotics"});
        //earth science
        lectureSubFieldTags.put(getLectureSubFields(2)[1], new String[]{"Agro-chemistry", "Analytical chemistry", "Astro-chemistry", "Atmospheric chemistry", "Biochemistry", "Chemical biology", "Chemical engineering", "Cheminformatics", "Computational chemistry", "Cosmochemistry", "Electro-chemistry", "Environmental chemistry", "Femto=chemistry", "Flavor", "Flow chemistry", "Geochemistry", "Green chemistry", "Histo-chemistry", "Hydrogenation", "Immunochemistry",
                "Inorganic chemistry", "Marine chemistry", "Mathematical chemistry", "Mechanochemistry", "Medicinal chemistry", "Molecular chemistry", "Molecular mechanics", "Nanotechnology", "Natural product chemistry", "Neurochemistry", "Oenology", "Organic chemistry", "Organometallic chemistry", "Petro-chemistry", "Pharmacology", "Photochemistry", "Physical chemistry", "Phyto-chemistry", "Polymer chemistry", "Quantum chemistry", "Radio-chemistry", "Solid state chemistry",
        "Sono-chemistry", "Supramolecualr chemistry", "Surface chemistry", "Synthetic chemistry", "Theoretical chemistry", "Thermo-chemistry"});

        lectureSubFieldTags.put(getLectureSubFields(2)[2], new String[]{"Edaphology", "Environmental chemistry", "Environmental science", "Gemology", "Geo-chemistry", "Geodesy", "Physical geography", "Atmospheric science(Meteorology)", "Biogeography(Phytogeography)", "Climatology(Paleoclimatology)", "Coastal geography(Oceanography)", "Edaphology/Pedology(Soil science)", "Geobiology", "Geology", "Mineralogy", "Petrology", "Sedimentology", "Speleology", "Tectonics", "Volcanology",
        "Geostatistics", "Hydrology/Limnology", "Landscape ecology", "Quaternary science", "Geophysics", "Paleontology", "Paleobiology", "Paleoecology"});
        //space science
        lectureSubFieldTags.put(getLectureSubFields(2)[3], new String[]{"Astro-biology", "Astronomy", "Observational astronomy", "Gamma ray astronomy", "Infrared astronomy", "Microwave astronomy", "Optical astronomy", "Radio astronomy", "UV astronomy", "X-ray astronomy", "Astrophysics", "Gravitational astronomy", "Black holes", "Interstellar medium", "Numerical simulations", "Astrophysical plasma", "Galaxy formation & evolution", "High-energy astro-physics", "Hydro-dynamics", "Magneto-hydrodynamcs",
        "Star formation", "Physical cosmology", "Stellar astrophysics", "Helio-seismology", "Stellar evolution", "Stellar nucleosynthesis", "Planetary science"});
        //physics
        lectureSubFieldTags.put(getLectureSubFields(2)[4], new String[]{"Acoustics", "Aerodynamics", "Applied physics", "Astro-physics", "Atomic, molecular & optical physics", "Biophysics", "Computational physics", "Condensed matter physics", "Cryogenics", "Electricity", "Electro-magnetism", "Elementary particle physics", "Experimental physics", "Fluid mechanics", "Geo-physics", "Mathematical physics", "Mechanics", "Medical physics", "Molecular physics", "Newtonian physics", "Nuclear physics", "Optics",
        "Plasma physics", "Quantum physics", "Solid mechanics", "Solid state physics", "Statistical mechanics", "Theoretical physics", "Thermal physics", "Thermodynamics"});



        //FORMAL SCIENCE
        //computer science
        lectureSubFieldTags.put(getLectureSubFields(3)[0], new String[]{"Loging in computer science", "Formal methods(formal verification)", "Logic programming", "Multi-valued logic", "Fuzzy logic", "Programming language semantics", "Type theory", "Algorithms", "Computational geometry", "Distirbuted algorithms", "Parallel algorithms", "Randomized algorithms", "Artificial intelligence", "Cognitive science", "Automated reasoning", "Computer vision",
        "Machine learning", "Artificial neural network", "Supportive vector machine", "Natural language processing(computational linguistics)", "Expert systems", "Robotics", "Data structures", "Computer architecture", "Computer graphics", "Image processing", "Scientific visualization", "Computer communications", "Cloud computing", "Information theory", "Internet, World Wide Web", "Ubiquitous computing", "Wireless computing(Mobile computing)", "Computer security & reliability",
        "Cryptography", "Fault-tolerant computing", "Computing in math, neural science, engineering & medicine", "Algebraic(Symbolic) computation", "Computational biology(Bioinformatics)", "Computational chemistry", "Computational mathematics", "Computational neuroscience", "Computational number theory", "Computational number theory", "Computational physics", "Computer aided engineering", "Computational fluid dynamics", "Finite element analysis", "Numerical analysis", "Scientific computing",
        "Computing in social sciences, arts & humanities", "Community informatics", "Computational economics", "Computational finance", "Computational sociology", "Digital humanities", "History of computer hardware", "History of computer science", "Humanistic informatics", "Database", "Distributed database", "Object database", "Relational database", "Data management", "Data mining", "Information architecture", "Information management", "Information retrieval", "Knowledge management", "Multimedia",
        "Hypermedia", "Sound & music computing", "Distributed computing", "Grid computing", "Human-computer interaction", "Operating systems", "Parallel computing", "High-performance computing", "Programming languages", "Compilers", "Programming paradigms", "Concurrent programming", "Functional programming", "Imperative programming", "Logic programming", "Objec oriented programming", "Program semantics", "Type theory", "Quantum computing", "Software engineering", "Theory of computation", "Automata theory(Formal languages)",
        "Comutability theory", "Computational complexity theory", "Concurrency theory", "VLSI design"});

        //mathematics
        lectureSubFieldTags.put(getLectureSubFields(3)[1], new String[]{"Pure mathematics", "Mathematical logig", "Foundations of mathematics", "Intuitionistic logic", "Modal logic", "Model theory", "Proof theory", "Recursion theory", "Set theory", "Algebra", "Associative algebra", "Category theory", "Topos theory", "Differential algebra", "Field theory", "Group theory", "Group representation", "Homological algebra", "K-theory", "Lattice(Order) theory", "Lie algebra", "Linear algebra", "Linear algebra(Vector space)",
                "Multi-linear algebra", "Non-associative algebra", "Representation theory", "Ring theory", "Commutative algebra", "Non-commutative algebra", "Universal algebra", "Analysis", "Complex analysis", "Functional analysis", "Operation theory", "Harmonic analysis", "Fourier analysis", "Non-standard analysis", "Ordinary differential equations", "P-acid analysis", "Partial differential equations", "Real analysis", "Calculus", "Probability theory", "Ergodic theory", "Measure theory", "Integral geometry", "Stochastic process",
        "Geometry & topology", "Affine geometry", "Algebraic geometry", "Algebraic topology", "Convex geometry", "Differential topology", "Discrete geometry", "Finite geometry", "Galois geometry", "General topology", "Geometric topology", "Integral geometry", "Non-commutative geometry", "Non-Educlidean geometry", "Projective geometry", "Number theory", "Algebraic number theory", "Arithmetic combinatorics", "Geometric number theory", "Applied mathematics", "Approximation theory", "Combinatorics", "Coding theory", "Cryptography", "Dynamical systems",
        "Dynamical systems", "Chaos theory", "Fractal geometry", "Game theory", "Graph theory", "Information theory", "Mathematical physics", "Quantum field theory", "Quantum gravity", "String theory", "Quantam mechanics", "Statistical mechanics", "Numerical analysis", "Operations research", "Assignment problems", "Decision analysis", "Dynamic programming", "Inventory theory", "Linear programming", "Mathematical optimization", "Optimal maintenance", "Real options analysis", "Scheduling", "Stochastic processes", "Systems analysis", "Statistics", "Actuarial science",
        "Demography", "Econometrics", "Mathematical statistics", "Data visualization", "Theory of computation", "Computational complexity theory"});

        //statistics
        lectureSubFieldTags.put(getLectureSubFields(3)[2], new String[]{"Astro statistics", "Bio statistics"});

        //APPLIED SCIENCE
        //business
        lectureSubFieldTags.put(getLectureSubFields(4)[0], new String[]{"Accounting", "Business management", "Finance", "Marketing", "Operations management"});

        //engineering & technology
        lectureSubFieldTags.put(getLectureSubFields(4)[1], new String[]{"Chemical engineering", "Bioengineering", "Biochemical engineering", "Bio-molecular engineering", "Catalysis", "Material engineering", "Molecular engineering", "Nanotechnology", "Polymer engineering", "Process design", "Petroleum engineering", "Nuclear engineering", "Food engineering", "Process engineering", "Reaction engineering", "Thermodynamics", "Transport phenomenon", "Civil engineering", "Coastal engineering", "Earthquake engineering", "Ecological engineering", "Environmental engineering",
        "Geo-technical engineering", "Engineering geology", "Hydraulic engineering", "Transportation engineering", "Highway engineering", "Structural engineering", "Architectural engineering", "Structure mechanics", "Surveying", "Educational technology", "Instructional design", "Distance education", "Instructional simulation", "Human performance technology", "Knowledge management", "Electrical engineering", "Applied physics", "Computer engineering", "Computer science", "Control systems engineering", "Control engineering", "Electronic engineering", "Instrumentation engineering",
        "Engineering physics", "Photonics", "Information theory", "Mechatronics", "Power engineering", "Quantum computing", "Roboics", "Semiconductors", "Telecommunication engineering", "Material science & engineering", "Biomaterials", "Ceramics engineering", "Crystallography", "Nano-materials", "Photonics", "Physical metallurgy", "Polymer engineering", "Polymer science", "Semiconductors", "Mechanical engineering", "Mechanical engineering", "Aeronautics", "Astronautics", "Acoustical engineering", "Automotive engineering", "Biomedical engineering", "Bio-mechanical engineering", "Neural engineering",
        "Continuum engineering", "Fluid mechnics", "Heat transfer", "Industrial engineering", "Manufacturing engineering", "Marine engineering", "Mass transfer", "Mechatronics", "Nano-engineering", "Ocean engineering", "Optical engineering", "Robotics", "Theromodynamics", "System science", "Chaos theory", "Complex systems", "Conceptual systems", "Control theory", "Affect control theory", "Control systems", "Control engineering", "Dynamic systems", "Perceptual control systems", "Cybermetics", "Bio-cybermetics", "Engineering cybermetics", "Management cybermetics", "Medical cybermetics", "New cybermetics",
        "Second order cybermetics", "Socio-cybermetics", "Network science", "Operation research", "Systems biology", "Computational systems biology","Synthetic  biology", "Systems immunology", "Systems neuroscience", "System dynamcis", "Social dynamics", "Systems ecology", "Ecosystem ecology", "Systems engineering", "Biological systems engineering", "Earth systems engineering & management", "Enterprise systems engineering", "Systems analysis", "Systems psychology", "Ergonomics", "Family systems theory", "Systemic therapy", "Systems theory", "Bio-mechanical systems theory", "Ecological systems theory", "Developmental systems theory",
        "General systems theory", "Living systems theory", "LTI systems theory", "Mathematical systems theory", "Socio-technology systems theory", "World systems theory", "Systems theory in anthropology"});

        //medicine & health
        lectureSubFieldTags.put(getLectureSubFields(4)[2], new String[]{});

    }


    static {
        vidPlacerAndFinder.put(getMainLectureField(0), getLectureSubFields(0));
        vidPlacerAndFinder.put(getMainLectureField(1), getLectureSubFields(1));
        vidPlacerAndFinder.put(getMainLectureField(1), getLectureSubFields(1));
        vidPlacerAndFinder.put(getMainLectureField(2), getLectureSubFields(2));
        vidPlacerAndFinder.put(getMainLectureField(3), getLectureSubFields(3));
        vidPlacerAndFinder.put(getMainLectureField(4), getLectureSubFields(4));
        vidPlacerAndFinder.put(getMainLectureField(5), getLectureSubFields(5));
        vidPlacerAndFinder.put(getMainLectureField(6), getLectureSubFields(6));
        vidPlacerAndFinder.put(getMainLectureField(7), getLectureSubFields(7));
        vidPlacerAndFinder.put(getMainLectureField(8), getLectureSubFields(8));
        vidPlacerAndFinder.put(getMainLectureField(9), getLectureSubFields(9));
        vidPlacerAndFinder.put(getMainLectureField(10), getLectureSubFields(10));
        vidPlacerAndFinder.put(getMainLectureField(11), getLectureSubFields(11));
        vidPlacerAndFinder.put(getMainLectureField(12), getLectureSubFields(12));
        vidPlacerAndFinder.put(getMainLectureField(13), getLectureSubFields(13));
        vidPlacerAndFinder.put(getMainLectureField(14), getLectureSubFields(14));
        vidPlacerAndFinder.put(getMainLectureField(15), getLectureSubFields(15));
        vidPlacerAndFinder.put(getMainLectureField(16), getLectureSubFields(16));
        vidPlacerAndFinder.put(getMainLectureField(17), getLectureSubFields(17));
        vidPlacerAndFinder.put(getMainLectureField(18), getLectureSubFields(18));



        knowledgeBase.add("Year 1");
        knowledgeBase.add("Year 2");
        knowledgeBase.add("Year 3");
        knowledgeBase.add("Year 4");
        knowledgeBase.add("Year 5");
    }



}
