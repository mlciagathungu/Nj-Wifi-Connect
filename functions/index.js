const functions = require("firebase-functions");
const axios = require("axios");

// Replace with your actual M-Pesa API endpoint and credentials.
exports.stkPush = functions.https.onCall(async (data, context) => {
  // Optionally, verify the user is authenticated:
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
  }

  const { phone_number, amount, account_reference, description } = data;

  try {
    // Call your M-Pesa API here (replace with your logic!)
    const response = await axios.post("YOUR_MPESA_API_URL", {
      phone_number,
      amount,
      account_reference,
      description,
    });

    return { success: true, result: response.data };
  } catch (error) {
    return { success: false, error: error.message };
  }
});