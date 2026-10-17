/**
 * Home Mess Tiffin Service - Android Studio Project Logic
 * Sir Parashurambhau College (SP College), Pune
 * Authors: Gaurav Kaluram Khaladkar (34305) & Aditay More (34302)
 * Guided by: Miss. Rutuja Mam
 */

// Global State
const state = {
  currentUser: null,
  cart: {},
  activeCategory: 'All',
  chefTab: 'to_prepare', // 'to_prepare' or 'prepared'
  dishes: [],
  orders: []
};

// Check if running inside Android WebView with Native Bridge
const isAndroidNative = () => typeof window.AndroidBridge !== 'undefined';

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
  initStorage();
  checkSession();
  bindEvents();
});

// Seed default fallback data in case native bridge is offline or in web test
function initStorage() {
  if (!localStorage.getItem('hm_dishes')) {
    const defaultDishes = [
      {
        id: 1,
        name: "Special Maharashtrian Veg Thali",
        description: "3 Fresh Chapatis, Dal Tadka, Sukhi Bhaji, Patal Bhaji, Steamed Basmati Rice, Gulab Jamun & Papad.",
        category: "Thali",
        price: 120.0,
        quantity: 50,
        image: "https://images.unsplash.com/photo-1610057099443-fde8c4d50f91?q=80&w=600&auto=format&fit=crop",
        is_available: true
      },
      {
        id: 2,
        name: "Special Puran Poli Thali",
        description: "2 Puran Poli with Pure Desi Ghee, Katachi Amti, Batata Sukhi Bhaji, Steamed Rice, Papad & Pickle.",
        category: "Special",
        price: 160.0,
        quantity: 30,
        image: "https://images.unsplash.com/photo-1546833999-b9f581a1996d?q=80&w=600&auto=format&fit=crop",
        is_available: true
      },
      {
        id: 3,
        name: "Student Budget Thali (Daily Mess)",
        description: "3 Chapatis, 1 Sabzi of the day, Dal Fry, Jeera Rice, and Fresh Green Salad.",
        category: "Thali",
        price: 80.0,
        quantity: 100,
        image: "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?q=80&w=600&auto=format&fit=crop",
        is_available: true
      },
      {
        id: 4,
        name: "Kolhapuri Chicken Tiffin",
        description: "Authentic spicy Kolhapuri Chicken Curry, 3 Jowar Bhakri/Chapatis, Tambda Rassa, and Indrayani Rice.",
        category: "Non-Veg",
        price: 180.0,
        quantity: 35,
        image: "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?q=80&w=600&auto=format&fit=crop",
        is_available: true
      },
      {
        id: 5,
        name: "Kanda Poha & Masala Chai",
        description: "Fresh breakfast kanda poha garnished with roasted peanuts, fresh coriander, grated coconut, and hot tea.",
        category: "Breakfast",
        price: 40.0,
        quantity: 80,
        image: "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?q=80&w=600&auto=format&fit=crop",
        is_available: true
      },
      {
        id: 6,
        name: "Healthy Diet Sprouts & Paneer Bowl",
        description: "Boiled moong sprouts, high-protein paneer cubes, cucumber, tomato, lime, and chaat masala.",
        category: "Diet",
        price: 90.0,
        quantity: 40,
        image: "https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=600&auto=format&fit=crop",
        is_available: true
      }
    ];
    localStorage.setItem('hm_dishes', JSON.stringify(defaultDishes));
  }

  if (!localStorage.getItem('hm_orders')) {
    const defaultOrders = [
      {
        id: 101,
        customerId: 1,
        customerName: "Gaurav Khaladkar (Student)",
        customerPhone: "9527298977",
        address: "Flat 402, Sai Residency, Tilak Road, Pune",
        items: [{ name: "Special Maharashtrian Veg Thali", qty: 2, price: 120.0 }],
        total: 240.0,
        status: "PREPARING",
        payment: "UPI (Google Pay)",
        date: "Today, 12:45 PM",
        otp: "4829"
      },
      {
        id: 102,
        customerId: 1,
        customerName: "Gaurav Khaladkar (Student)",
        customerPhone: "9527298977",
        address: "Flat 402, Sai Residency, Tilak Road, Pune",
        items: [{ name: "Kanda Poha & Masala Chai", qty: 1, price: 40.0 }],
        total: 40.0,
        status: "OUT_FOR_DELIVERY",
        payment: "Cash on Delivery",
        date: "Today, 08:30 AM",
        otp: "7193"
      }
    ];
    localStorage.setItem('hm_orders', JSON.stringify(defaultOrders));
  }
}

// Session Management
function checkSession() {
  const savedUser = localStorage.getItem('hm_session');
  if (savedUser) {
    state.currentUser = JSON.parse(savedUser);
    renderRoleView();
  } else {
    showView('view-login');
  }
}

function bindEvents() {
  // Login Form Submission
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', (e) => {
      e.preventDefault();
      handleLogin();
    });
  }

  // Register Form Submission
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', (e) => {
      e.preventDefault();
      handleRegister();
    });
  }

  // Post Dish Form Submission
  const postDishForm = document.getElementById('postDishForm');
  if (postDishForm) {
    postDishForm.addEventListener('submit', (e) => {
      e.preventDefault();
      handlePostDish();
    });
  }
}

// Navigation View Switcher
function showView(viewId) {
  document.querySelectorAll('.app-view').forEach(v => v.classList.add('d-none'));
  const target = document.getElementById(viewId);
  if (target) {
    target.classList.remove('d-none');
    window.scrollTo(0, 0);
  }
}

// Render Appropriate Dashboard based on User Role
function renderRoleView() {
  if (!state.currentUser) {
    showView('view-login');
    return;
  }

  document.querySelectorAll('.user-display-name').forEach(el => el.textContent = state.currentUser.name);

  if (state.currentUser.role === 'chef') {
    showView('view-chef');
    renderChefScreen();
  } else if (state.currentUser.role === 'delivery') {
    showView('view-delivery');
    renderDeliveryScreen();
  } else {
    // Default: Customer / Student
    showView('view-customer');
    loadDishes();
    renderCart();
  }
}

// ==================== AUTHENTICATION ====================

function handleLogin() {
  const ident = document.getElementById('loginIdentifier').value.trim();
  const pass = document.getElementById('loginPassword').value.trim();

  if (!ident || !pass) {
    toast("Please enter your Email or Phone and Password");
    return;
  }

  if (isAndroidNative()) {
    try {
      const resp = JSON.parse(window.AndroidBridge.loginUser(ident, pass));
      if (resp.success) {
        state.currentUser = resp;
        localStorage.setItem('hm_session', JSON.stringify(resp));
        toast("Login Successful! Welcome " + resp.name);
        renderRoleView();
        return;
      } else {
        toast(resp.message || "Invalid credentials");
      }
    } catch (e) {
      console.warn("AndroidBridge error:", e);
    }
  }

  // Fallback demo logins if offline
  let user = null;
  if (ident.toLowerCase().includes('chef') || ident === '9822012345') {
    user = { id: 2, name: "Annapurna Home Mess (Chef)", role: "chef", email: "chef@gmail.com", phone: "9822012345" };
  } else if (ident.toLowerCase().includes('delivery') || ident === '9765432100') {
    user = { id: 3, name: "Rahul Shinde (Delivery Rider)", role: "delivery", email: "delivery@gmail.com", phone: "9765432100" };
  } else {
    user = { id: 1, name: "Gaurav Khaladkar (Student)", role: "customer", email: "customer@gmail.com", phone: "9527298977", address: "Flat 402, Sai Residency, Tilak Road, Pune" };
  }

  state.currentUser = user;
  localStorage.setItem('hm_session', JSON.stringify(user));
  toast("Logged in as " + user.name);
  renderRoleView();
}

// Quick 1-Click Demo Login for Testing in Android Studio
function quickLogin(role) {
  if (role === 'customer') {
    document.getElementById('loginIdentifier').value = "customer@gmail.com";
    document.getElementById('loginPassword').value = "pass123";
  } else if (role === 'chef') {
    document.getElementById('loginIdentifier').value = "chef@gmail.com";
    document.getElementById('loginPassword').value = "pass123";
  } else if (role === 'delivery') {
    document.getElementById('loginIdentifier').value = "delivery@gmail.com";
    document.getElementById('loginPassword').value = "pass123";
  }
  handleLogin();
}

function handleRegister() {
  const name = document.getElementById('regName').value.trim();
  const email = document.getElementById('regEmail').value.trim();
  const phone = document.getElementById('regPhone').value.trim();
  const pass = document.getElementById('regPassword').value.trim();
  const role = document.getElementById('regRole').value;
  const address = document.getElementById('regAddress').value.trim();
  const area = document.getElementById('regArea').value.trim();
  const pin = document.getElementById('regPincode').value.trim();
  const city = document.getElementById('regCity').value.trim();

  if (!name || !email || !phone || !pass) {
    toast("Please fill all mandatory fields.");
    return;
  }

  if (isAndroidNative()) {
    try {
      const resp = JSON.parse(window.AndroidBridge.registerUser(name, email, phone, pass, role, address, area, pin, city));
      if (resp.success) {
        toast("Account created! Please sign in.");
        showView('view-login');
        return;
      }
    } catch (e) {
      console.warn("AndroidBridge error:", e);
    }
  }

  const newUser = { id: Date.now(), name, email, phone, role, address, area, pin, city };
  state.currentUser = newUser;
  localStorage.setItem('hm_session', JSON.stringify(newUser));
  toast("Registration successful!");
  renderRoleView();
}

function logout() {
  state.currentUser = null;
  state.cart = {};
  localStorage.removeItem('hm_session');
  toast("Signed out successfully.");
  showView('view-login');
}

// ==================== DISHES & MENU ====================

function loadDishes() {
  if (isAndroidNative()) {
    try {
      const nativeDishes = JSON.parse(window.AndroidBridge.getDishes(state.activeCategory));
      if (nativeDishes && nativeDishes.length > 0) {
        state.dishes = nativeDishes;
        renderDishesList();
        return;
      }
    } catch (e) {
      console.warn("AndroidBridge getDishes error:", e);
    }
  }

  // Fallback storage
  const stored = JSON.parse(localStorage.getItem('hm_dishes') || '[]');
  state.dishes = state.activeCategory === 'All' 
    ? stored 
    : stored.filter(d => d.category.toLowerCase() === state.activeCategory.toLowerCase());
  renderDishesList();
}

function filterCategory(cat) {
  state.activeCategory = cat;
  document.querySelectorAll('.category-pill').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.category === cat);
  });
  loadDishes();
}

function renderDishesList() {
  const container = document.getElementById('dishesContainer');
  if (!container) return;

  if (state.dishes.length === 0) {
    container.innerHTML = `
      <div class="text-center py-5">
        <div style="font-size: 48px;">🍲</div>
        <h6 class="mt-2 text-muted">No dishes currently available in this category.</h6>
      </div>`;
    return;
  }

  container.innerHTML = state.dishes.map(dish => {
    const qty = state.cart[dish.id] ? state.cart[dish.id].qty : 0;
    const isVeg = dish.category !== 'Non-Veg';

    return `
      <div class="dish-card">
        <div class="dish-img-wrapper">
          <img src="${dish.image.startsWith('http') || dish.image.startsWith('data:') ? dish.image : 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?q=80&w=600&auto=format&fit=crop'}" alt="${dish.name}" loading="lazy" />
          <span class="dish-category-badge">${dish.category}</span>
          <span class="dish-veg-badge">
            <span style="display:inline-block; width:12px; height:12px; border-radius:50%; background:${isVeg ? '#2e7d32' : '#c62828'};"></span>
          </span>
        </div>
        <div class="dish-body">
          <div class="dish-title">${dish.name}</div>
          <div class="dish-desc">${dish.description}</div>
          <div class="dish-footer">
            <div class="dish-price">₹${dish.price.toFixed(2)}</div>
            ${qty > 0 ? `
              <div class="qty-controller">
                <button class="qty-btn" onclick="updateCartItem(${dish.id}, -1)">−</button>
                <span class="qty-val">${qty}</span>
                <button class="qty-btn" onclick="updateCartItem(${dish.id}, 1)">+</button>
              </div>
            ` : `
              <button class="btn btn-sm btn-outline-warning text-dark fw-bold px-3 py-1 rounded-pill" onclick="addToCart(${dish.id})">
                + ADD
              </button>
            `}
          </div>
        </div>
      </div>
    `;
  }).join('');
}

// ==================== CART & ORDER PLACEMENT ====================

function addToCart(dishId) {
  const dish = state.dishes.find(d => d.id === dishId);
  if (!dish) return;

  state.cart[dishId] = {
    dish: dish,
    qty: 1
  };
  vibrate(40);
  renderDishesList();
  renderCart();
}

function updateCartItem(dishId, change) {
  if (!state.cart[dishId]) return;

  state.cart[dishId].qty += change;
  if (state.cart[dishId].qty <= 0) {
    delete state.cart[dishId];
  }
  vibrate(30);
  renderDishesList();
  renderCart();
}

function renderCart() {
  const keys = Object.keys(state.cart);
  const cartBar = document.getElementById('cartFloatingBar');
  
  if (keys.length === 0) {
    if (cartBar) cartBar.classList.add('d-none');
    return;
  }

  let totalQty = 0;
  let totalPrice = 0;
  keys.forEach(id => {
    const item = state.cart[id];
    totalQty += item.qty;
    totalPrice += item.qty * item.dish.price;
  });

  if (cartBar) {
    cartBar.classList.remove('d-none');
    document.getElementById('cartItemsCount').textContent = `${totalQty} Tiffin Item${totalQty > 1 ? 's' : ''}`;
    document.getElementById('cartTotalPrice').textContent = `₹${totalPrice.toFixed(2)}`;
  }
}

function openCheckoutModal() {
  const keys = Object.keys(state.cart);
  if (keys.length === 0) {
    toast("Your Tiffin Cart is empty!");
    return;
  }

  let subtotal = 0;
  const itemsHtml = keys.map(id => {
    const item = state.cart[id];
    const itemCost = item.qty * item.dish.price;
    subtotal += itemCost;
    return `
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <div class="fw-bold fs-6">${item.dish.name}</div>
          <small class="text-muted">₹${item.dish.price} × ${item.qty}</small>
        </div>
        <div class="fw-bold">₹${itemCost.toFixed(2)}</div>
      </div>
    `;
  }).join('');

  const deliveryFee = 20.0;
  const grandTotal = subtotal + deliveryFee;

  document.getElementById('checkoutItemsList').innerHTML = itemsHtml;
  document.getElementById('checkoutSubtotal').textContent = `₹${subtotal.toFixed(2)}`;
  document.getElementById('checkoutGrandTotal').textContent = `₹${grandTotal.toFixed(2)}`;
  
  if (state.currentUser && state.currentUser.address) {
    document.getElementById('checkoutAddress').value = state.currentUser.address;
  }

  const modal = new bootstrap.Modal(document.getElementById('checkoutModal'));
  modal.show();
}

function placeFinalOrder() {
  const address = document.getElementById('checkoutAddress').value.trim();
  const paymentMode = document.querySelector('input[name="paymentMethod"]:checked').value;

  if (!address) {
    toast("Please enter your delivery address!");
    return;
  }

  const keys = Object.keys(state.cart);
  let total = 20.0; // Delivery fee
  const orderItems = keys.map(id => {
    const it = state.cart[id];
    total += it.qty * it.dish.price;
    return { name: it.dish.name, qty: it.qty, price: it.dish.price };
  });

  const otp = String(Math.floor(1000 + Math.random() * 9000));
  const newOrder = {
    id: Date.now() % 100000,
    customerId: state.currentUser ? state.currentUser.id : 1,
    customerName: state.currentUser ? state.currentUser.name : "Student Customer",
    customerPhone: state.currentUser ? state.currentUser.phone : "9527298977",
    address: address,
    items: orderItems,
    total: total,
    status: "PLACED",
    payment: paymentMode,
    date: "Just Now",
    otp: otp
  };

  if (isAndroidNative()) {
    try {
      window.AndroidBridge.placeOrder(
        newOrder.customerId,
        newOrder.customerName,
        newOrder.customerPhone,
        address,
        JSON.stringify(orderItems),
        total,
        paymentMode
      );
    } catch (e) {
      console.warn("AndroidBridge placeOrder error:", e);
    }
  }

  // Save to local storage
  const stored = JSON.parse(localStorage.getItem('hm_orders') || '[]');
  stored.unshift(newOrder);
  localStorage.setItem('hm_orders', JSON.stringify(stored));

  // Clear Cart
  state.cart = {};
  renderCart();
  renderDishesList();

  // Close Checkout Modal
  bootstrap.Modal.getInstance(document.getElementById('checkoutModal')).hide();

  // Show Success Alert & Jump to Live Tracking
  toast(`Order #${newOrder.id} Placed! Delivery OTP: ${otp}`);
  viewOrderTracking(newOrder.id);
}

// ==================== LIVE ORDER TRACKING ====================

function viewOrderTracking(orderId) {
  const stored = JSON.parse(localStorage.getItem('hm_orders') || '[]');
  const order = stored.find(o => o.id === orderId) || stored[0];

  if (!order) {
    toast("No active order found.");
    return;
  }

  document.getElementById('trackingOrderId').textContent = `#${order.id}`;
  document.getElementById('trackingOtp').textContent = order.otp || '4829';
  document.getElementById('trackingAddress').textContent = order.address;
  document.getElementById('trackingTotal').textContent = `₹${order.total.toFixed(2)}`;

  // Update Stepper Timeline matching Page 15 DFD
  const steps = ['PLACED', 'ACCEPTED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
  const curIdx = steps.indexOf(order.status.toUpperCase());

  document.querySelectorAll('.step-node').forEach((node, idx) => {
    node.classList.remove('completed', 'active');
    if (idx < curIdx) {
      node.classList.add('completed');
    } else if (idx === curIdx) {
      node.classList.add('active');
    }
  });

  const modal = new bootstrap.Modal(document.getElementById('trackingModal'));
  modal.show();
}

// ==================== CHEF / MESS OWNER SCREEN ====================

function switchChefNav(tab) {
  state.chefTab = tab;
  document.querySelectorAll('.chef-nav-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.tab === tab);
  });

  document.getElementById('chefHomeSection').classList.toggle('d-none', tab !== 'home');
  document.getElementById('chefOrdersSection').classList.toggle('d-none', tab !== 'orders');
  document.getElementById('chefPostDishSection').classList.toggle('d-none', tab !== 'post_dish');

  if (tab === 'orders') {
    renderChefOrders();
  }
}

function switchChefOrdersSubTab(subTab) {
  document.getElementById('btnTabToPrepare').classList.toggle('btn-primary', subTab === 'to_prepare');
  document.getElementById('btnTabToPrepare').classList.toggle('btn-outline-primary', subTab !== 'to_prepare');
  document.getElementById('btnTabPrepared').classList.toggle('btn-primary', subTab === 'prepared');
  document.getElementById('btnTabPrepared').classList.toggle('btn-outline-primary', subTab !== 'prepared');

  renderChefOrders(subTab);
}

function renderChefScreen() {
  switchChefNav('orders');
}

function renderChefOrders(subTab = 'to_prepare') {
  const container = document.getElementById('chefOrdersContainer');
  if (!container) return;

  const stored = JSON.parse(localStorage.getItem('hm_orders') || '[]');
  const filtered = stored.filter(o => {
    if (subTab === 'to_prepare') {
      return o.status === 'PLACED' || o.status === 'ACCEPTED' || o.status === 'PREPARING';
    } else {
      return o.status === 'OUT_FOR_DELIVERY' || o.status === 'DELIVERED';
    }
  });

  if (filtered.length === 0) {
    container.innerHTML = `
      <div class="text-center py-5">
        <div style="font-size: 42px;">📋</div>
        <p class="text-muted mt-2">No orders in this queue.</p>
      </div>`;
    return;
  }

  container.innerHTML = filtered.map(order => `
    <div class="card mb-3 border-0 shadow-sm rounded-4 p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Order #${order.id}</span>
        <span class="fw-bold text-success">₹${order.total.toFixed(2)}</span>
      </div>
      <div class="fw-bold fs-6">${order.customerName}</div>
      <div class="small text-muted mb-2"><i class="bi bi-geo-alt"></i> ${order.address}</div>
      <div class="bg-light p-2 rounded-3 mb-3 small">
        ${order.items.map(it => `<div>• ${it.name} × ${it.qty}</div>`).join('')}
      </div>
      <div class="d-flex gap-2">
        ${order.status === 'PLACED' ? `
          <button class="btn btn-sm btn-success flex-grow-1" onclick="updateOrderStatusAction(${order.id}, 'ACCEPTED')">Accept Order</button>
        ` : ''}
        ${order.status === 'ACCEPTED' ? `
          <button class="btn btn-sm btn-primary flex-grow-1" onclick="updateOrderStatusAction(${order.id}, 'PREPARING')">Send to Kitchen</button>
        ` : ''}
        ${order.status === 'PREPARING' ? `
          <button class="btn btn-sm btn-warning flex-grow-1 text-dark fw-bold" onclick="updateOrderStatusAction(${order.id}, 'OUT_FOR_DELIVERY')">Ready for Rider</button>
        ` : ''}
        <button class="btn btn-sm btn-outline-secondary" onclick="callPhone('${order.customerPhone}')">📞 Call</button>
      </div>
    </div>
  `).join('');
}

function handlePostDish() {
  const name = document.getElementById('dishNameInput').value.trim();
  const desc = document.getElementById('dishDescInput').value.trim();
  const category = document.getElementById('dishCategorySelect').value;
  const qty = parseInt(document.getElementById('dishQtyInput').value) || 25;
  const price = parseFloat(document.getElementById('dishPriceInput').value) || 100.0;
  const imageInput = document.getElementById('dishImagePreview');
  const imageUrl = imageInput && imageInput.src ? imageInput.src : "https://images.unsplash.com/photo-1546833999-b9f581a1996d?q=80&w=600&auto=format&fit=crop";

  if (!name || !price) {
    toast("Dish Name and Price are required!");
    return;
  }

  const newDish = {
    id: Date.now() % 100000,
    name,
    description: desc || "Fresh authentic homemade mess preparation.",
    category,
    price,
    quantity: qty,
    image: imageUrl,
    is_available: true
  };

  if (isAndroidNative()) {
    try {
      window.AndroidBridge.postDish(1, name, desc, category, price, qty, imageUrl);
    } catch (e) {
      console.warn("AndroidBridge postDish error:", e);
    }
  }

  const stored = JSON.parse(localStorage.getItem('hm_dishes') || '[]');
  stored.unshift(newDish);
  localStorage.setItem('hm_dishes', JSON.stringify(stored));

  toast(`Dish "${name}" posted successfully to Home Mess menu!`);
  document.getElementById('postDishForm').reset();
  switchChefNav('orders');
}

// ==================== DELIVERY AGENT SCREEN ====================

function renderDeliveryScreen() {
  const container = document.getElementById('deliveryOrdersContainer');
  if (!container) return;

  const stored = JSON.parse(localStorage.getItem('hm_orders') || '[]');
  const activeOrders = stored.filter(o => o.status === 'OUT_FOR_DELIVERY' || o.status === 'PREPARING' || o.status === 'ACCEPTED');

  if (activeOrders.length === 0) {
    container.innerHTML = `
      <div class="text-center py-5">
        <div style="font-size: 42px;">🛵</div>
        <p class="text-muted mt-2">No pending deliveries right now. You are ready to receive orders!</p>
      </div>`;
    return;
  }

  container.innerHTML = activeOrders.map(order => `
    <div class="card mb-3 border-0 shadow-sm rounded-4 p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="badge bg-danger px-3 py-2 rounded-pill">Pickup Order #${order.id}</span>
        <span class="badge bg-secondary">${order.payment}</span>
      </div>
      <div class="small mb-2">
        <strong>From:</strong> Annapurna Home Mess, Tilak Road<br/>
        <strong>To:</strong> ${order.address} (${order.customerName})
      </div>
      <div class="d-flex gap-2">
        ${order.status === 'OUT_FOR_DELIVERY' ? `
          <button class="btn btn-sm btn-success flex-grow-1" onclick="verifyDeliveryOtp(${order.id}, '${order.otp}')">Verify OTP & Deliver</button>
        ` : `
          <button class="btn btn-sm btn-primary flex-grow-1" onclick="updateOrderStatusAction(${order.id}, 'OUT_FOR_DELIVERY')">Accept & Start Ride</button>
        `}
        <button class="btn btn-sm btn-outline-dark" onclick="callPhone('${order.customerPhone}')">📞 Call</button>
      </div>
    </div>
  `).join('');
}

function verifyDeliveryOtp(orderId, expectedOtp) {
  const entered = prompt(`Ask Customer for 4-digit Delivery OTP (Demo OTP: ${expectedOtp}):`);
  if (entered === expectedOtp) {
    updateOrderStatusAction(orderId, 'DELIVERED');
    toast(`Order #${orderId} delivered successfully! Earnings added.`);
    renderDeliveryScreen();
  } else if (entered !== null) {
    toast("Invalid OTP! Please check with customer.");
  }
}

function updateOrderStatusAction(orderId, newStatus) {
  if (isAndroidNative()) {
    try {
      window.AndroidBridge.updateOrderStatus(orderId, newStatus);
    } catch (e) {
      console.warn("AndroidBridge updateOrderStatus error:", e);
    }
  }

  const stored = JSON.parse(localStorage.getItem('hm_orders') || '[]');
  const order = stored.find(o => o.id === orderId);
  if (order) {
    order.status = newStatus;
    localStorage.setItem('hm_orders', JSON.stringify(stored));
    toast(`Order #${orderId} updated to ${newStatus}`);
  }

  if (state.currentUser.role === 'chef') {
    renderChefOrders();
  } else if (state.currentUser.role === 'delivery') {
    renderDeliveryScreen();
  }
}

// ==================== HARDWARE & HELPERS ====================

function callPhone(num) {
  if (isAndroidNative()) {
    try {
      window.AndroidBridge.makePhoneCall(num);
      return;
    } catch (e) {
      console.warn("Call error:", e);
    }
  }
  window.open(`tel:${num}`, '_system');
}

function toast(msg) {
  if (isAndroidNative()) {
    try {
      window.AndroidBridge.showToast(msg);
      return;
    } catch (ignored) {}
  }

  let box = document.getElementById('appToastBox');
  if (!box) {
    box = document.createElement('div');
    box.id = 'appToastBox';
    box.style.cssText = "position:fixed; top:20px; left:50%; transform:translateX(-50%); z-index:9999; background:#1e293b; color:#fff; padding:10px 20px; border-radius:24px; font-size:13px; box-shadow:0 6px 18px rgba(0,0,0,0.3); transition:all 0.3s; opacity:0;";
    document.body.appendChild(box);
  }
  box.textContent = msg;
  box.style.opacity = "1";
  box.style.top = "30px";
  setTimeout(() => {
    box.style.opacity = "0";
    box.style.top = "20px";
  }, 2400);
}

function vibrate(ms) {
  if (isAndroidNative()) {
    try {
      window.AndroidBridge.vibrateDevice(ms);
    } catch (ignored) {}
  } else if (navigator.vibrate) {
    navigator.vibrate(ms);
  }
}

function showProjectInfoModal() {
  const modal = new bootstrap.Modal(document.getElementById('projectInfoModal'));
  modal.show();
}

function previewDishImage(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function(e) {
      const img = document.getElementById('dishImagePreview');
      if (img) {
        img.src = e.target.result;
        img.classList.remove('d-none');
      }
    };
    reader.readAsDataURL(file);
  }
}
