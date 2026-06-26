// ============================================
// Bookstore AI — Landing Page Scripts
// ============================================

// ---------- Navbar scroll effect ----------
window.addEventListener('scroll', () => {
    const nav = document.querySelector('.navbar');
    nav.classList.toggle('scrolled', window.scrollY > 50);
});

// ---------- Smooth scroll for anchor links ----------
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
        // Close mobile menu if open
        document.querySelector('.nav-menu').classList.remove('active');
        document.querySelector('.hamburger').classList.remove('active');
    });
});

// ---------- Hamburger menu toggle ----------
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');

hamburger.addEventListener('click', () => {
    hamburger.classList.toggle('active');
    navMenu.classList.toggle('active');
});

// ---------- Counter animation ----------
const animateCounter = (el) => {
    const target = parseInt(el.getAttribute('data-target'));
    const duration = 2000;
    const start = performance.now();

    const update = (now) => {
        const progress = Math.min((now - start) / duration, 1);
        const eased = 1 - Math.pow(1 - progress, 3); // ease out cubic
        el.textContent = Math.floor(target * eased).toLocaleString();
        if (progress < 1) requestAnimationFrame(update);
    };
    requestAnimationFrame(update);
};

const counterObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            animateCounter(entry.target);
            counterObserver.unobserve(entry.target);
        }
    });
}, { threshold: 0.5 });

document.querySelectorAll('.counter-value').forEach(el => counterObserver.observe(el));

// ---------- Fade-in animation ----------
const fadeObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
        }
    });
}, { threshold: 0.1 });

document.querySelectorAll('.fade-in').forEach(el => fadeObserver.observe(el));

// ---------- Pricing toggle (monthly/annual) ----------
const pricingToggle = document.querySelector('.pricing-toggle');

const updatePricing = () => {
    const isAnnual = pricingToggle.checked;

    document.querySelectorAll('.price-amount').forEach(el => {
        const monthly = el.getAttribute('data-monthly');
        const annual = el.getAttribute('data-annual');
        el.textContent = isAnnual ? annual : monthly;
    });

    document.querySelectorAll('.price-period').forEach(el => {
        el.textContent = isAnnual ? '/año' : '/mes';
    });

    // Update toggle labels
    const labels = document.querySelectorAll('.pricing-toggle-wrapper span');
    if (labels.length >= 2) {
        labels[0].classList.toggle('active-label', !isAnnual);
        labels[1].classList.toggle('active-label', isAnnual);
    }
};

if (pricingToggle) {
    pricingToggle.addEventListener('change', updatePricing);
}
